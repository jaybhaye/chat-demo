package com.test.grpc.chat.auth;

import java.util.HashMap;

import com.auth0.jwt.JWTSigner;
import com.auth0.jwt.JWTVerifier;
import com.test.grpc.chat.types.exceptions.AuthenticationFailedException;

public class AuthTokenManager {
	
	private static final String SECRET = 	"1w9KYh89B3JyRcr6BzCS\n"  
											+ "J9aXjNm2UCi9cebLzDNr\n"  
											+ "JvBEJKSZUCwPBqu0mz40\n"  
											+ "N8ym1r5L74djx24v8okQ\n"  
											+ "KoIl8jgeYZe4Mkjzsxrp";
	
	private static final JWTVerifier VERIFIER = new JWTVerifier(SECRET);;
	private static final JWTSigner SIGNER = new JWTSigner(SECRET);
	private static final int TOKEN_VALIDITY_IN_SECONDS = 60 * 60;
	
	public static String createAuthToken(String userId) {
		
	    long iat = System.currentTimeMillis() / 1000l; 
	    long exp = iat + TOKEN_VALIDITY_IN_SECONDS; 
	    HashMap<String, Object> claims = new HashMap<String, Object>();
	    
	    claims.put("exp", exp);
	    claims.put("iat", iat);
	    claims.put("sub", userId);

	    return SIGNER.sign(claims);
	  }
	
	public static void verify(String token) throws AuthenticationFailedException{
		try {
			VERIFIER.verify(token);
		} catch (Exception e) {
			throw new AuthenticationFailedException("Authentication Failed");
		}
	}
}
