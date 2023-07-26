package com.auth.controller;

import java.security.Principal;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.auth.exception.UnAuthorizedException;
import com.auth.exception.UserExistsException;
import com.auth.exception.UserNotFoundException;
import com.auth.model.AppUser;
import com.auth.model.PublicUserDetails;
import com.auth.model.UserProfileImage;
import com.auth.service.AppUserService;
import com.auth.service.UserProfileImageService;

@RestController
@RequestMapping("/api/user")
public class UserController {

	private static final String DESCRIPTION = "description";
	private static final String ERROR = "error";
	@Autowired
	private AppUserService appUserService;
	@Autowired
	private UserProfileImageService userProfileImageService;
	
	@PostMapping
	public ResponseEntity<UserControllerResponse> createUser(
			@RequestBody PublicUserDetails requestUser,
			Principal principal) {
		AppUser appUser = new AppUser();
		try {
			appUser.setUsername(requestUser.getUserName());
			appUser.setPassword(requestUser.getPassword());
			appUser.setDob(requestUser.getDob());
			Integer userId = appUserService.createUser(appUser);
			requestUser.setId(userId);
			requestUser.setPassword("*****");
		} 
		catch (UserExistsException e) {
			e.printStackTrace();
			Map<String, String> message = Map.of(ERROR, e.getMessage());
			return ResponseEntity.badRequest()
								 .body(new UserControllerResponse(message, null));
		}
		catch (Exception e) {
			e.printStackTrace();
			Map<String, String> message = Map.of(ERROR, e.getMessage());
			return ResponseEntity.internalServerError()
								 .body(new UserControllerResponse(message, null));
		}
		Map<String, String> message = Map.of(DESCRIPTION, "Created user");
		return new ResponseEntity<UserControllerResponse>(
				new UserControllerResponse(message, requestUser),
				HttpStatus.CREATED);
	}
	
	@GetMapping("me")
	public ResponseEntity<UserControllerResponse> me(Principal principal) {
		if(principal == null) {
			throw new AuthenticationCredentialsNotFoundException("Not authenticated");
		}
		PublicUserDetails user = new PublicUserDetails();
		try {
			AppUser appUser = appUserService.findByUserName(principal.getName());
			if(appUser == null) {
				throw new UserNotFoundException("No user available with the current session details");
			}
			user.setUserName(appUser.getUsername());
			user.setPassword("*****");
			user.setDob(appUser.getDob());
			user.setId(appUser.getId());
		}
		catch (AuthenticationCredentialsNotFoundException e) {
			e.printStackTrace();
			Map<String, String> message = Map.of(ERROR, e.getMessage());
			return new ResponseEntity<UserControllerResponse>(
					new UserControllerResponse(message, null),
					HttpStatus.valueOf(403));
		}
		catch (UserNotFoundException e) {
			e.printStackTrace();
			Map<String, String> message = Map.of(ERROR, e.getMessage());
			return new ResponseEntity<UserControllerResponse>(
					new UserControllerResponse(message, null),
					HttpStatus.NOT_FOUND);
		}
		catch (Exception e) {
			e.printStackTrace();
			Map<String, String> message = Map.of(ERROR, e.getMessage());
			return ResponseEntity.internalServerError()
								 .body(new UserControllerResponse(message, null));
		}
		Map<String, String> message = Map.of(DESCRIPTION, "success");
		return ResponseEntity.ok()
							 .body(new UserControllerResponse(message, user));
	}
	@GetMapping("me/profile-image")
	public ResponseEntity<byte[]> getProfileImage(Principal principal) {
		if(principal == null) {
			throw new AuthenticationCredentialsNotFoundException("Not authenticated");
		}
		byte[] profileImageBytes = null;
		try {
			AppUser appUser = appUserService.findByUserName(principal.getName());
			profileImageBytes = userProfileImageService.getImage(appUser.getId(), 
					"profile.png");
			
			return ResponseEntity.ok()
							.contentType(MediaType.valueOf("image/png"))
							.body(profileImageBytes);
		}
		catch (Exception e) {
			e.printStackTrace();
			Map<String, String> message = Map.of(ERROR, e.getMessage());
			return ResponseEntity.internalServerError()
								 .body(null);
		}
	}
	@GetMapping("{id}")
	public ResponseEntity<UserControllerResponse> getUser(
			@PathVariable Integer id, Principal principal) {
		if(principal == null) {
			throw new AuthenticationCredentialsNotFoundException("Not authenticated");
		}
		PublicUserDetails user = new PublicUserDetails();
		try {
			AppUser appUser = appUserService.findByUserId(id);
			if(appUser == null) {
				throw new UserNotFoundException("No user available with the given id");
			}
			System.out.println(principal.getName() + ": " + appUser.getUsername());
			if(!principal.getName().equals(appUser.getUsername())) {
				throw new UnAuthorizedException("You don't have permissions to view this");
			}
			user.setUserName(appUser.getUsername());
			user.setPassword("*****");
			user.setDob(appUser.getDob());
			user.setId(appUser.getId());
		}
		catch (AuthenticationCredentialsNotFoundException e) {
			e.printStackTrace();
			Map<String, String> message = Map.of(ERROR, e.getMessage());
			return new ResponseEntity<UserControllerResponse>(
					new UserControllerResponse(message, null),
					HttpStatus.valueOf(403));
		}
		catch (UserNotFoundException e) {
			e.printStackTrace();
			Map<String, String> message = Map.of(ERROR, e.getMessage());
			return new ResponseEntity<UserControllerResponse>(
					new UserControllerResponse(message, null),
					HttpStatus.NOT_FOUND);
		}
		catch (UnAuthorizedException e) {
			e.printStackTrace();
			Map<String, String> message = Map.of(ERROR, e.getMessage());
			return new ResponseEntity<UserControllerResponse>(
					new UserControllerResponse(message, null),
					HttpStatus.UNAUTHORIZED);
		}
		catch (Exception e) {
			e.printStackTrace();
			Map<String, String> message = Map.of(ERROR, e.getMessage());
			return ResponseEntity.internalServerError()
								 .body(new UserControllerResponse(message, null));
		}
		Map<String, String> message = Map.of(DESCRIPTION, "success");
		return ResponseEntity.ok()
							 .body(new UserControllerResponse(message, user));
	}
	
	 
	private record UserControllerResponse(Map<String, String> message, 
										@Nullable PublicUserDetails user) {}
	
}
