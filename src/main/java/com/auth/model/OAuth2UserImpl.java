package com.auth.model;

import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class OAuth2UserImpl implements OAuth2User {

	private Map<String, Object> attributes;
	private Collection<? extends GrantedAuthority> authorities;
	private String name;
		
	@Override
	public Map<String, Object> getAttributes() {
		return attributes;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getName() {
		return name;
	}

	public OAuth2UserImpl(Map<String, Object> attributes, Collection<? extends GrantedAuthority> authorities,
			String name) {
		super();
		this.attributes = attributes;
		this.authorities = authorities;
		this.name = name;
	}
}
