package com.auth.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.auth.model.UserProfileImage;

public interface UserProfileImageRepository extends JpaRepository<UserProfileImage, Long> {
	
	Optional<UserProfileImage> findByImageName(String imageName);
	
	Optional<UserProfileImage> findByImageNameAndAppUserId(String imageName, Integer userId);
}
