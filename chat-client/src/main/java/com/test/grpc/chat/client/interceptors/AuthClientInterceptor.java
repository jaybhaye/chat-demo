package com.test.grpc.chat.client.interceptors;

import com.google.gson.GsonBuilder;
import com.test.grpc.chat.client.types.constants.Constants;

import io.grpc.CallOptions;
import io.grpc.Channel;
import io.grpc.ClientCall;
import io.grpc.ClientInterceptor;
import io.grpc.ForwardingClientCall;
import io.grpc.Metadata;
import io.grpc.MethodDescriptor;

public class AuthClientInterceptor implements ClientInterceptor {

	@Override
	public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(MethodDescriptor<ReqT, RespT> method,
			CallOptions callOptions, Channel next) {
		return new ForwardingClientCall.SimpleForwardingClientCall<ReqT, RespT>(next.newCall(method, callOptions)) {
			@Override
			public void start(Listener<RespT> responseListener, Metadata headers) {
				headers.put(Constants.USER_ID_METADATA_KEY, Constants.USER_ID_CONTEXT_KEY.get());
				if (Constants.PASSWORD_CONTEXT_KEY.get() != null) {

					headers.put(Constants.PASSWORD_METADATA_KEY, Constants.PASSWORD_CONTEXT_KEY.get());
				} 
				if (Constants.AUTH_TOKEN_CONTEXT_KEY.get() != null) {
					headers.put(Constants.AUTH_TOKEN_METADATA_KEY, Constants.AUTH_TOKEN_CONTEXT_KEY.get());
				}
				super.start(responseListener, headers);
			}
		};
	}
}
