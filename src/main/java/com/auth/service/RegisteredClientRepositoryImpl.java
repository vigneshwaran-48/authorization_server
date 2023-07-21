package com.auth.service;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.jackson2.SecurityJackson2Modules;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.jackson2.OAuth2AuthorizationServerJackson2Module;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.auth.model.Client;
import com.auth.repository.ClientRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import static com.auth.AuthServerUtils.*;

@Service
public class RegisteredClientRepositoryImpl implements RegisteredClientRepository {

	@Autowired
	private ClientRepository clientRepository;

	private ObjectMapper objectMapper = new ObjectMapper();

	public RegisteredClientRepositoryImpl() {
		ClassLoader classLoader = RegisteredClientRepositoryImpl.class.getClassLoader();
		List<com.fasterxml.jackson.databind.Module> securityModules = SecurityJackson2Modules.getModules(classLoader);
		this.objectMapper.registerModules(securityModules);
		this.objectMapper.registerModule(new OAuth2AuthorizationServerJackson2Module());
	}

	@Override
	public void save(RegisteredClient registeredClient) {
		Assert.notNull(registeredClient, "Registered Client can't be null");
		Client client = toClient(registeredClient);
		clientRepository.save(client);
	}

	@Override
	public RegisteredClient findById(String id) {
		return clientRepository.findById(id).map(this::toRegisteredClient).orElse(null);
	}

	@Override
	public RegisteredClient findByClientId(String clientId) {
		System.out.println("Searching client ......................... for client id => " + clientId);
		RegisteredClient client = clientRepository.findByClientId(clientId).map(this::toRegisteredClient).orElse(null);
		System.out.println("Founded client => " + client);
		return client;
	}

	private RegisteredClient toRegisteredClient(Client client) {
		Set<String> clientAuthMethods = StringUtils.commaDelimitedListToSet(client.getClientAuthenticationMethods());
		Set<String> authGrantTypes = StringUtils.commaDelimitedListToSet(client.getAuthorizationGrantTypes());
		Set<String> redirectUris = StringUtils.commaDelimitedListToSet(client.getRedirectUris());
		Set<String> clientScopes = StringUtils.commaDelimitedListToSet(client.getScopes());

		RegisteredClient.Builder builder = RegisteredClient.withId(client.getId())
				.clientAuthenticationMethods(
						authenticationMethods -> clientAuthMethods.forEach(authenticationMethod -> authenticationMethods
								.add(resolveClientAuthenticationMethod(authenticationMethod))))
				.authorizationGrantTypes((grantTypes) -> authGrantTypes
						.forEach(grantType -> grantTypes.add(resolveAuthorizationGrantType(grantType))))
				.clientId(client.getClientId()).clientIdIssuedAt(client.getClientIdIssuedAt())
				.clientName(client.getClientName()).clientSecret(client.getClientSecret())
				.clientSecretExpiresAt(client.getClientSecretExpiresAt())
				.redirectUris(uri -> redirectUris.forEach(uri::add)).scopes(scope -> clientScopes.forEach(scope::add));

		Map<String, Object> clientSettingsMap = parseMap(client.getClientSettings());
		builder.clientSettings(ClientSettings.withSettings(clientSettingsMap).build());

		Map<String, Object> tokenSettingsMap = parseMap(client.getTokenSettings());
		builder.tokenSettings(TokenSettings.withSettings(tokenSettingsMap).build());

		return builder.build();
	}

	private Client toClient(RegisteredClient registeredClient) {
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

	private Map<String, Object> parseMap(String data) {
		try {
			return this.objectMapper.readValue(data, new TypeReference<Map<String, Object>>() {
			});
		} catch (Exception ex) {
			throw new IllegalArgumentException(ex.getMessage(), ex);
		}
	}

	private String writeMap(Map<String, Object> data) {
		try {
			return this.objectMapper.writeValueAsString(data);
		} catch (Exception ex) {
			throw new IllegalArgumentException(ex.getMessage(), ex);
		}
	}

}
