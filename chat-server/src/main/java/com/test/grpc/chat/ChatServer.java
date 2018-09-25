package com.test.grpc.chat;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.test.grpc.chat.interceptors.APIAuthenticationInterceptor;
import com.test.grpc.chat.server.impl.ChatServiceImpl;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.ServerInterceptors;

public class ChatServer {

  private static final Logger LOG = LogManager.getLogger(ChatServer.class);
  private static final int PORT = 8091;
  
  public static void main(String[] args) throws InterruptedException, IOException {
    Server server = ServerBuilder.forPort(PORT)
    		.addService(ServerInterceptors.intercept(new ChatServiceImpl(), new APIAuthenticationInterceptor()))
    		.build();
    LOG.info(String.format("Started Server On Port %s", PORT));
    server.start();
    server.awaitTermination();
  }
}
