package com.test.grpc.chat.types.constants;

import static io.grpc.Metadata.ASCII_STRING_MARSHALLER;

import io.grpc.Context;
import io.grpc.Metadata;

public interface Constants {
	public static final Metadata.Key<String> USER_ID_METADATA_KEY = Metadata.Key.of("userId", ASCII_STRING_MARSHALLER);
	public static final Metadata.Key<String> AUTH_TOKEN_METADATA_KEY = Metadata.Key.of("authToken", ASCII_STRING_MARSHALLER);
	public static final Metadata.Key<String> PASSWORD_METADATA_KEY = Metadata.Key.of("password", ASCII_STRING_MARSHALLER);
	public static final Context.Key<String> USER_ID_CONTEXT_KEY = Context.key("userId");
	public static final Context.Key<String> AUTH_TOKEN_CONTEXT_KEY = Context.key("authToken");
}
