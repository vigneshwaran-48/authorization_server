package com.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.auth.model.Authority;

public interface AuthorityRepository extends JpaRepository<Authority, Integer> {

	Authority findByAuthority(String authority);
}
