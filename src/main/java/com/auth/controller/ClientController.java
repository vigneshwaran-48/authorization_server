package com.auth.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.auth.AuthServerUtils;
import com.auth.model.UserProfileImage;
import com.auth.repository.UserProfileImageRepository;
import com.auth.service.RegisteredClientRepositoryImpl;

@RestController
@RequestMapping("/api/client")
public class ClientController {

	@Autowired
	private RegisteredClientRepository registeredClientRepository;
	@Autowired
	private TokenSettings tokenSettings;
	@Autowired
	private ClientSettings clientSettings;
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	@Autowired
	private UserDetailsManager userDetailsManager;
	@Autowired
	private UserProfileImageRepository userImageRepository;
	
	private ClientAuthenticationMethod getClientAuthForString(String clientAuthStr) {
		switch (clientAuthStr) {
			case "client_secret_basic":
				return ClientAuthenticationMethod.CLIENT_SECRET_BASIC;
			case "client_secret_post": 
				return ClientAuthenticationMethod.CLIENT_SECRET_POST;
		default:
			return ClientAuthenticationMethod.CLIENT_SECRET_JWT;
		}
	}
	
	@PostMapping("create")
	public ResponseEntity<RegisteredClient> addClient(@RequestBody ClientCreationPayload payload) {
		List<AuthorizationGrantType> authGrantTypes = Arrays.asList(payload.grantTypes.split(","))
							.stream()
							.map(AuthServerUtils::resolveAuthorizationGrantType)
							.collect(Collectors.toList());
		List<String> redirectUris = Arrays.asList(payload.redirectUris.split(","));
		List<String> scopes = Arrays.asList(payload.scopes.split(","));
				
		RegisteredClient client = 
				RegisteredClient.withId(UUID.randomUUID().toString())
					.clientId(payload.clientId)
					.clientSecret(passwordEncoder.encode(payload.clientSecret))
					.clientAuthenticationMethod(getClientAuthForString(payload.clientAuthMethods))
					.clientName(payload.clientName)
					.scopes(scope -> scopes.forEach(scope::add))
					.redirectUris(redirect -> redirectUris.forEach(redirect::add))
					.authorizationGrantTypes(grantType -> authGrantTypes.forEach(grantType::add))
					.tokenSettings(tokenSettings)
					.clientSettings(clientSettings)
					.build();
		registeredClientRepository.save(client);
		
		
		return ResponseEntity.of(Optional.of(client));
	}
	
//	@GetMapping("{clientId}")
//	public ResponseEntity<RegisteredClient> getClientById(@PathVariable String clientId) {
//		RegisteredClient client = registeredClientRepository.findByClientId(clientId);
//		return ResponseEntity.of(Optional.of(client));
//	}
	
	@PostMapping("test")
	public String test(@RequestParam("image") MultipartFile file) {
//		RegisteredClient registeredClient = RegisteredClient.withId(UUID.randomUUID().toString())
//				.clientId("todo-client")
//				.clientSecret(passwordEncoder.encode("todo-password"))
//				.scope("read")
//				.scope(OidcScopes.OPENID)
//				.scope(OidcScopes.PROFILE)
//				.redirectUri("http://127.0.0.1:9191")
//				.redirectUri("http://127.0.0.1:9191/login/oauth2/code/spring")
//				.clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
//				.authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
//				.authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
//				.authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
//				.tokenSettings(tokenSettings)
//				.clientSettings(clientSettings)
//				.build();
//		
//		registeredClientRepository.save(registeredClient);
//		UserDetails details = User.withUsername("vicky")
//					   .password(passwordEncoder.encode("vicky@123"))
//					   .roles("ADMIN")
//					   .build();
//		
//		userDetailsManager.createUser(details);
		try {
			UserProfileImage userImage = new UserProfileImage();
			userImage.setImageName(file.getOriginalFilename());
			userImage.setType(file.getContentType());
			userImage.setImageBytes(file.getBytes());
			
			userImage = userImageRepository.save(userImage);
			System.out.println("Image id => " + userImage.getId());
		} catch (IOException e) {
			e.printStackTrace();
		}
//		userImageRepository.sav
		return "test successfull";
	}
	@GetMapping("{imageName}")
	public ResponseEntity<byte[]> getImage(@PathVariable String imageName) {
		System.out.println("Searchin for image");
		UserProfileImage userImage = userImageRepository.findByImageName(imageName).orElseThrow();
		return ResponseEntity.ok()
							 .contentType(MediaType.valueOf(userImage.getType()))
							 .body(userImage.getImageBytes());
	}
	record ClientCreationPayload(String redirectUris, String scopes, String clientName,
								 String grantTypes, String clientAuthMethods,
								 String clientId, String clientSecret ) {}
}
