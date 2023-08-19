package com.auth.service;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.auth.model.AppUser;
import com.auth.model.AuthProvider;
import com.auth.model.OAuth2AuthenticationProcessingException;
import com.auth.model.OAuth2UserImpl;
import com.auth.model.oauth.OAuth2UserInfo;
import com.auth.model.oauth.OAuth2UserInfoFactory;
import com.auth.repository.UserRepository;

public class CustomOAuth2Service extends DefaultOAuth2UserService {

	@Autowired
	private UserRepository userRepository;
	
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		OAuth2User oAuth2User = super.loadUser(userRequest);
		
		try {
			return processRequest(oAuth2User, userRequest);
		}
		catch (Exception e) {
			e.printStackTrace();
			//IMPORTANT
			// Throwing an instance of AuthenticationException will trigger the OAuth2AuthenticationFailureHandler
            throw new InternalAuthenticationServiceException(e.getMessage(), e.getCause());
		}
	}
	
	private OAuth2User processRequest(OAuth2User oauth2User, OAuth2UserRequest oauth2Request) {
		
		OAuth2UserInfo userInfo = OAuth2UserInfoFactory
				.getOAuth2UserInfo(oauth2Request.getClientRegistration().getRegistrationId(),
								   oauth2User.getAttributes());
		if(userInfo.getEmail() == null || userInfo.getEmail().isBlank()) {
            throw new OAuth2AuthenticationProcessingException("Email not found from OAuth2 provider");
        }
		
		AppUser appUser = userRepository.findByEmail(userInfo.getEmail()).orElse(null);
		
		if(appUser != null) {
			if(!appUser.getProvider().equals(AuthProvider.valueOf(oauth2Request
					.getClientRegistration().getRegistrationId().toUpperCase()))) {
				throw new OAuth2AuthenticationProcessingException("Looks like you're signed up with " +
                        appUser.getProvider() + " account. Please use your " + appUser.getProvider() +
                        " account to login.");
			}
			appUser = updateExistingUser(appUser, userInfo);
		}
		else {
			appUser = registerNewUser(oauth2Request, userInfo);
		}
		List<GrantedAuthority> authorities = Collections.
                singletonList(new SimpleGrantedAuthority("ROLE_USER"));
		
		String name = appUser.getId() != null ? appUser.getId().toString() : String.valueOf(-1);

		return new OAuth2UserImpl(oauth2User.getAttributes(), authorities, name);
	}
	private AppUser registerNewUser(OAuth2UserRequest oAuth2UserRequest, OAuth2UserInfo oAuth2UserInfo) {
		AppUser user = new AppUser();

        user.setProvider(AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId()));
        user.setProviderId(oAuth2UserInfo.getId());
        user.setUsername(oAuth2UserInfo.getName());
        user.setEmail(oAuth2UserInfo.getEmail());
        //TODO Need to add this image in db...
//        user.set(oAuth2UserInfo.getImageUrl());
        return userRepository.save(user);
    }

    private AppUser updateExistingUser(AppUser existingUser, OAuth2UserInfo oAuth2UserInfo) {
        existingUser.setUsername(oAuth2UserInfo.getName());
        existingUser.setEmail(oAuth2UserInfo.getEmail());
      //TODO Need to update this image in db...
//        existingUser.setImageUrl(oAuth2UserInfo.getImageUrl());
        return userRepository.save(existingUser);
    }
}
