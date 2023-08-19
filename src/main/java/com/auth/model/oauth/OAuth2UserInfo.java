package com.auth.model.oauth;

import java.util.Map;

public abstract class OAuth2UserInfo {

	protected Map<String, Object> attributes;
	
	public OAuth2UserInfo(Map<String, Object> attriutes) {
		this.attributes = attriutes;
	}
	
	public abstract String getId();

    public abstract String getName();

    public abstract String getEmail();

    public abstract String getImageUrl();
}
