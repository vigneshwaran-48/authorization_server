package com.auth.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.auth.model.Authorization;

public interface AuthorizationRepository extends JpaRepository<Authorization, String> {
	
	Optional<Authorization> findByState(String state);
	
	Optional<Authorization> findByAuthorizationCodeValue(String authorizationCodeValue);
	
	Optional<Authorization> findByAccessTokenValue(String accessTokenValue);
	
	Optional<Authorization> findByRefreshTokenValue(String refreshTokenValue);
	
	@Query("select a from Authorization a where a.state = :token" +
			" or a.authorizationCodeValue = :token" +
			" or a.accessTokenValue = :token" +
			" or a.refreshTokenValue = :token"
	)
	Optional<Authorization> findByStateOrAuthorizationCodeValueOrAccessTokenValueOrRefreshTokenValue(@Param("token") String token);
}
