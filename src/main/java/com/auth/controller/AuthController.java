package com.auth.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class AuthController {
//	
	@PostMapping("/oauth2/authorize")
	public void login() {
		System.out.println("Post authorize invoked .......");
	}
	
//	@PostMapping("/authenticate")
//	public void authenticate(@RequestBody LoginCredintialsRequest request) {
//		System.out.println(request.userName);
//	}
	
	record LoginCredintialsRequest(String userName, String password) {}
}
