package com.auth.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.auth.model.Client;

public interface ClientRepository extends JpaRepository<Client, String> {
	
	Optional<Client> findByClientId(String clientId);
	
	void deleteByClientId(String clientId);
}
