package com.auth.service;

import org.springframework.stereotype.Service;

import com.auth.model.Client;

@Service
public interface ClientService {

	String addClient(Client client) throws Exception;
	
	boolean isClientExists(String clientId);
	
	void removeClient(String clientId);
}
