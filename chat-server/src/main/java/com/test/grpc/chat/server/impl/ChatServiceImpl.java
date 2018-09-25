package com.test.grpc.chat.server.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.test.grpc.chat.Chat.AuthRequest;
import com.test.grpc.chat.Chat.AuthResponse;
import com.test.grpc.chat.Chat.SendMessageRequest;
import com.test.grpc.chat.Chat.SendMessageResponse;
import com.test.grpc.chat.Chat.Status;
import com.test.grpc.chat.auth.AuthenticationController;
import com.test.grpc.chat.message.MessageRouter;
import com.test.grpc.chat.preprocessors.PreprocesorFactory;
import com.test.grpc.chat.types.Client;
import com.test.grpc.chat.types.constants.Constants;
import com.test.grpc.chat.types.exceptions.ChainProcessingException;
import com.test.grpc.chat.types.exceptions.MessageRateExceededException;

import com.test.grpc.chat.*;

import io.grpc.stub.StreamObserver;

public class ChatServiceImpl extends ChatServiceGrpc.ChatServiceImplBase {

  private static final Logger LOG = LogManager.getLogger(ChatServiceImpl.class);
	
  private AuthenticationController authController = new AuthenticationController();
  
  @Override
  public StreamObserver<Chat.ChatMessage> receiveMessage(StreamObserver<Chat.ChatMessage> responseObserver) {
	  
    String userId = Constants.USER_ID_CONTEXT_KEY.get();
    try {
    	MessageRouter.getInstance().onClientConnect(new Client().withClientId(userId).withConnection(responseObserver));
    } catch (Exception e) {
    	LOG.info("Noop, ignore");
    }
    
    
    // Noop Stream Observer
    return new StreamObserver<Chat.ChatMessage>() {
        @Override
        public void onNext(Chat.ChatMessage value) {}

        @Override
        public void onError(Throwable t) {}

        @Override
        public void onCompleted() {}
      };
    
  }
  
  @Override
  public void login(AuthRequest request, StreamObserver<AuthResponse> responseObserver) {
    
	  String token = authController.getToken(request.getUserId(), request.getPassword());
	  
	  Status status = (token != null) ? Status.SUCCESS : Status.FAILUERE;
	  String message = (token != null) ? "Authentication Successful" : "Access Denied";
	  AuthResponse.Builder builder = Chat.AuthResponse.newBuilder()
			  										  .setStatus(status)
			  										  .setErrorMessage(message);
	  if(token != null) {
		  builder.setAuthToken(token);
	  }
	  
	  AuthResponse response = builder.build();
	  responseObserver.onNext(response);
	  responseObserver.onCompleted();
  }
  
  @Override
  public void sendMessage(SendMessageRequest request, StreamObserver<SendMessageResponse> responseObserver) {
	  String message = request.getMessage();
	  String from = request.getSender();
	  String to = request.getReceiver();
	  
	  Chat.ChatMessage chatMessage = Chat.ChatMessage.newBuilder()
			  										 .setSender(from)
			  										 .setMessage(message)
			  										 .setReceiver(to)
			  										 .build();
	  SendMessageResponse response = null;
	  try {
		  PreprocesorFactory.newSendMessageProcessorChain().execute(request);
		  MessageRouter.getInstance().sendOrStoreMessage(to, chatMessage);
		  response = Chat.SendMessageResponse.newBuilder()
					     .setErrorMessage("")
					     .setStatus(Status.SUCCESS)
					     .build();
		  responseObserver.onNext(response);
	  } catch (MessageRateExceededException e) {
		  response = Chat.SendMessageResponse.newBuilder()
					     .setErrorMessage("Message Rate Limit Exceeded")
					     .setStatus(Status.FAILUERE)
					     .build();
		  responseObserver.onNext(response);
	  } catch (ChainProcessingException e) {
		  response = Chat.SendMessageResponse.newBuilder()
					     .setErrorMessage("Message size more than maximum allowed")
					     .setStatus(Status.FAILUERE)
					     .build();
		  responseObserver.onNext(response);
	  } catch (Exception e) {
		  response = Chat.SendMessageResponse.newBuilder()
					     .setErrorMessage("Error")
					     .setStatus(Status.FAILUERE)
					     .build();
		  responseObserver.onNext(response);
	  }
	  responseObserver.onCompleted();
  }
}
