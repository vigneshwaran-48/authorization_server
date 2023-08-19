package com.auth.model.oauth;

import java.util.Map;

import com.auth.model.AuthProvider;
import com.auth.model.OAuth2AuthenticationProcessingException;

public class OAuth2UserInfoFactory {

	public static OAuth2UserInfo getOAuth2UserInfo(String registrationId, 
			Map<String, Object> attributes) {
		
		if(registrationId.equalsIgnoreCase(AuthProvider.GITHUB.toString())) {
			return new GithubOAuth2UserInfo(attributes);
		}
		else {
			throw new OAuth2AuthenticationProcessingException("Sorry! Login with " + registrationId 
					+ " is not supported yet.");
		}
	}
}
