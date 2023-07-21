package com.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.auth.model.AppUser;

public interface UserRepository extends JpaRepository<AppUser, Integer> {
	
	AppUser findByUsername(String username);
	
	void deleteByUsername(String username);
}
