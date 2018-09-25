package com.test.grpc.chat.interceptors;

import com.test.grpc.chat.auth.AuthenticationController;
import com.test.grpc.chat.types.constants.Constants;
import com.test.grpc.chat.types.exceptions.AuthenticationFailedException;

import io.grpc.Context;
import io.grpc.Contexts;
import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.ServerCall.Listener;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;
import io.grpc.Status;

public class APIAuthenticationInterceptor implements ServerInterceptor {

	private static final ServerCall.Listener NOOP_LISTENER = new ServerCall.Listener() {};
	private AuthenticationController controller = new AuthenticationController();
	@Override
	public <ReqT, RespT> Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> call, Metadata metadata,
			ServerCallHandler<ReqT, RespT> next) {
		
	    String userId = metadata.get(Constants.USER_ID_METADATA_KEY);
	    String authToken = metadata.get(Constants.AUTH_TOKEN_METADATA_KEY);
	    String password = metadata.get(Constants.PASSWORD_METADATA_KEY);
	    
	    if (userId == null && (authToken == null || password == null)) {
	      call.close(Status.UNAUTHENTICATED.withDescription("Must Supply User ID and (Auth Token Or Password)"), metadata);
	      return NOOP_LISTENER;
	    }
	    
	    
	    Context currentContext;
	    try {
	    	
	    	if(authToken != null) {
	    		controller.verify(authToken);
	    	} else {
	    		String token = controller.getToken(userId, password);
	    		if(token == null) {
	    			throw new AuthenticationFailedException("Authentication Failed.");
	    		}
	    	}
	      
	      currentContext = Context.current()
	    		                  .withValue(Constants.USER_ID_CONTEXT_KEY, userId)
	                              .withValue(Constants.AUTH_TOKEN_CONTEXT_KEY, authToken);
	    } catch (Exception e) {
	      call.close(Status.UNAUTHENTICATED.withDescription(e.getMessage()).withCause(e), metadata);
	      return NOOP_LISTENER;
	    }
	    return Contexts.interceptCall(currentContext, call, metadata, next);
	}
}
