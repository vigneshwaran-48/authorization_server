package com.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.auth.model.Client;
import com.auth.repository.ClientRepository;

@Service
public class ClientServiceImpl implements ClientService {

	@Autowired
	private ClientRepository clientRepository;
	
	@Override
	public String addClient(Client client) throws Exception {
		Assert.notNull(client, "Client can't be null");
		Client addedClient = clientRepository.save(client);
		if(addedClient != null) {
			return addedClient.getId();
		}
		throw new Exception("Can't create the client");
	}

	@Override
	public boolean isClientExists(String clientId) {
		Assert.notNull(clientId, "Client id can't be null");
		return clientRepository.findByClientId(clientId).isPresent();
	}

	@Override
	public void removeClient(String clientId) {
		Assert.notNull(clientId, "Client id can't be null");
		clientRepository.deleteByClientId(clientId);
	}

	
}
