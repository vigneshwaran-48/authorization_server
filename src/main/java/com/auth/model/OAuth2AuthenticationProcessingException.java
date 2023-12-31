package com.auth.model;

import org.springframework.security.core.AuthenticationException;

public class OAuth2AuthenticationProcessingException extends AuthenticationException {

	private static final long serialVersionUID = 1L;
	
	public OAuth2AuthenticationProcessingException(String msg) {
		super(msg);
	}
	public OAuth2AuthenticationProcessingException(String msg, Throwable t) {
        super(msg, t);
    }

}
