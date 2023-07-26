package com.auth.service;

import org.springframework.stereotype.Service;

import com.auth.model.UserProfileImage;

@Service
public interface UserProfileImageService {

	byte[] getImage(Integer userId, String imageName);
	
	void uploadImage(UserProfileImage userImage);
}
