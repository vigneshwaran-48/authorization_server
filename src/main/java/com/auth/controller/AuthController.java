package com.auth.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class AuthController {
//	
	@PostMapping("/oauth2/authorize")
	public void login() {
		System.out.println("Post authorize invoked .......");
	}
}
