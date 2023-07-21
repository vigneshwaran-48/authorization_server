package com.auth.service;

import com.auth.exception.UserExistsException;
import com.auth.model.AppUser;

public interface AppUserService {

	Integer createUser(AppUser appUser) throws UserExistsException;
	
	void deleteUser(Integer userId);
	
	void updateUser(AppUser appUser);
	
	AppUser findByUserId(Integer id);
	
	AppUser findByUserName(String name);
}
