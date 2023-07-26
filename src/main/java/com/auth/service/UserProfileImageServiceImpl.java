package com.auth.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.auth.model.UserProfileImage;
import com.auth.repository.UserProfileImageRepository;

@Service
public class UserProfileImageServiceImpl implements UserProfileImageService {

	@Autowired
	private UserProfileImageRepository userProfileImageRepository;
	
	@Override
	public byte[] getImage(Integer userId, String imageName) {
		byte[] profileImageBytes = null;
		UserProfileImage profileImage = userProfileImageRepository
				.findByImageNameAndAppUserId(imageName, userId).orElse(getDefaultImage());
		
		profileImageBytes = profileImage.getImageBytes();
		return profileImageBytes;
	}
	
	@Override
	public void uploadImage(UserProfileImage userImage) {
		// TODO Auto-generated method stub
		
	}
	private UserProfileImage getDefaultImage() {
		UserProfileImage userProfileImage = userProfileImageRepository.findById(-1L).orElseThrow();
		return userProfileImage;
	}
}	
