package com.auth.model;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.security.jackson2.SecurityJackson2Modules;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.jackson2.OAuth2AuthorizationServerJackson2Module;
import org.springframework.util.StringUtils;

import com.auth.common.model.CommonClientDetails;
import com.auth.service.RegisteredClientRepositoryImpl;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "client")
public class Client {
	
	private static ObjectMapper objectMapper = new ObjectMapper();
	
	@Id
	private String id;
	
	private String clientId;
	private Instant clientIdIssuedAt;
	private String clientSecret;
	private Instant clientSecretExpiresAt;
	private String clientName;
	
	@Column(length = 1000)
	private String clientAuthenticationMethods;
	@Column(length = 1000)
	private String authorizationGrantTypes;
	@Column(length = 1000)
	private String redirectUris;
	@Column(length = 1000)
	private String scopes;
	@Column(length = 2000)
	private String clientSettings;
	@Column(length = 2000)
	private String tokenSettings;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getClientId() {
		return clientId;
	}
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	public Instant getClientIdIssuedAt() {
		return clientIdIssuedAt;
	}
	public void setClientIdIssuedAt(Instant clientIdIssuedAt) {
		this.clientIdIssuedAt = clientIdIssuedAt;
	}
	public String getClientSecret() {
		return clientSecret;
	}
	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}
	public Instant getClientSecretExpiresAt() {
		return clientSecretExpiresAt;
	}
	public void setClientSecretExpiresAt(Instant clientSecretExpiresAt) {
		this.clientSecretExpiresAt = clientSecretExpiresAt;
	}
	public String getClientName() {
		return clientName;
	}
	public void setClientName(String clientName) {
		this.clientName = clientName;
	}
	public String getClientAuthenticationMethods() {
		return clientAuthenticationMethods;
	}
	public void setClientAuthenticationMethods(String clientAuthenticationMethods) {
		this.clientAuthenticationMethods = clientAuthenticationMethods;
	}
	public String getAuthorizationGrantTypes() {
		return authorizationGrantTypes;
	}
	public void setAuthorizationGrantTypes(String authorizationGrantTypes) {
		this.authorizationGrantTypes = authorizationGrantTypes;
	}
	public String getRedirectUris() {
		return redirectUris;
	}
	public void setRedirectUris(String redirectUris) {
		this.redirectUris = redirectUris;
	}
	public String getScopes() {
		return scopes;
	}
	public void setScopes(String scopes) {
		this.scopes = scopes;
	}
	public String getClientSettings() {
		return clientSettings;
	}
	public void setClientSettings(String clientSettings) {
		this.clientSettings = clientSettings;
	}
	public String getTokenSettings() {
		return tokenSettings;
	}
	public void setTokenSettings(String tokenSettings) {
		this.tokenSettings = tokenSettings;
	}
	public Client(String id, String clientId, Instant clientIdIssuedAt, String clientSecret,
			Instant clientSecretExpiresAt, String clientName, String clientAuthenticationMethods,
			String authorizationGrantTypes, String redirectUris, String scopes, String clientSettings,
			String tokenSettings) {
		super();
		this.id = id;
		this.clientId = clientId;
		this.clientIdIssuedAt = clientIdIssuedAt;
		this.clientSecret = clientSecret;
		this.clientSecretExpiresAt = clientSecretExpiresAt;
		this.clientName = clientName;
		this.clientAuthenticationMethods = clientAuthenticationMethods;
		this.authorizationGrantTypes = authorizationGrantTypes;
		this.redirectUris = redirectUris;
		this.scopes = scopes;
		this.clientSettings = clientSettings;
		this.tokenSettings = tokenSettings;
	}
	public Client() {}
	
	public static Client toClient(CommonClientDetails commonClientDetails) {
		
		Client client = new Client();
		
		client.setId(commonClientDetails.getId());
		client.setClientId(commonClientDetails.getClientId());
		client.setClientIdIssuedAt(commonClientDetails.getClientIdIssuedAt());
		client.setClientSecret(commonClientDetails.getClientSecret());
		client.setClientSecretExpiresAt(commonClientDetails.getClientSecretExpiresAt());
		client.setClientName(commonClientDetails.getClientName());
		client.setClientAuthenticationMethods(commonClientDetails.getClientAuthenticationMethods());
		client.setAuthorizationGrantTypes(commonClientDetails.getAuthorizationGrantTypes());
		client.setRedirectUris(commonClientDetails.getRedirectUris());
		client.setScopes(commonClientDetails.getScopes());
		client.setClientSettings(commonClientDetails.getClientSettings());
		client.setTokenSettings(commonClientDetails.getTokenSettings());
		
		return client;
	}
	public static Client toClient(RegisteredClient registeredClient) {
		List<String> clientAuthMethods = registeredClient.getClientAuthenticationMethods().stream()
				.map(ClientAuthenticationMethod::getValue).collect(Collectors.toList());
		List<String> authorizationGrantTypes = registeredClient.getAuthorizationGrantTypes().stream()
				.map(AuthorizationGrantType::getValue).collect(Collectors.toList());

		Client client = new Client();
		client.setId(registeredClient.getId());
		client.setClientId(registeredClient.getClientId());
		client.setClientName(registeredClient.getClientName());
		client.setClientSecret(registeredClient.getClientSecret());
		client.setClientSecretExpiresAt(registeredClient.getClientSecretExpiresAt());
		client.setClientIdIssuedAt(registeredClient.getClientIdIssuedAt());
		client.setClientAuthenticationMethods(StringUtils.collectionToCommaDelimitedString(clientAuthMethods));
		client.setAuthorizationGrantTypes(StringUtils.collectionToCommaDelimitedString(authorizationGrantTypes));
		client.setRedirectUris(StringUtils.collectionToCommaDelimitedString(registeredClient.getRedirectUris()));
		client.setScopes(StringUtils.collectionToCommaDelimitedString(registeredClient.getScopes()));
		client.setClientSettings(writeMap(registeredClient.getClientSettings().getSettings()));
		client.setTokenSettings(writeMap(registeredClient.getTokenSettings().getSettings()));

		return client;
	}
	
	private static String writeMap(Map<String, Object> data) {
		try {
			return objectMapper.writeValueAsString(data);
		} catch (Exception ex) {
			throw new IllegalArgumentException(ex.getMessage(), ex);
		}
	}
	
	static {
		ClassLoader classLoader = Client.class.getClassLoader();
		List<com.fasterxml.jackson.databind.Module> securityModules = SecurityJackson2Modules.getModules(classLoader);
		objectMapper.registerModules(securityModules);
		objectMapper.registerModule(new OAuth2AuthorizationServerJackson2Module());
	}
}
