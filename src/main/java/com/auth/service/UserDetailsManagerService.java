package com.auth.service;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

import com.auth.exception.UserExistsException;
import com.auth.model.AppUser;
import com.auth.model.Authority;
import com.auth.repository.AuthorityRepository;
import com.auth.repository.UserRepository;

@Service
public class UserDetailsManagerService implements UserDetailsManager, AppUserService {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired 
	private AuthorityRepository authorityRepository;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		System.out.println("Loading user ..................");
		AppUser appUser = userRepository.findByUsername(username);
		Collection<GrantedAuthority> authorities = new HashSet<>();
		appUser.getAuthorities().forEach(authority -> {
			authorities.add(new SimpleGrantedAuthority(authority.getAuthority()));
		});
		
		User user = new User(appUser.getUsername(), appUser.getPassword(),
							 appUser.getEnabled(), appUser.getAccountNonExpired(),
							 appUser.getCredentialsNonExpired(), appUser.getAccountNonExpired(),
							 authorities);
		return user;
	}

	@Override
	public void createUser(UserDetails user) {
		AppUser appUser = toAppUser(user);
		userRepository.save(appUser);
	}

	@Override
	public void updateUser(UserDetails user) {
		AppUser appUser = toAppUser(user);
		userRepository.save(appUser);
	}

	@Override
	public void deleteUser(String username) {
		userRepository.deleteByUsername(username);
	}

	@Override
	public void changePassword(String oldPassword, String newPassword) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean userExists(String username) {
		System.out.println("finding user ......................");
		AppUser appUser = userRepository.findByUsername(username);
		if(appUser != null && appUser.getUsername().equals(username)) {
			return true;
		}
		return false;
	}

	private AppUser toAppUser(UserDetails user) {
		Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
		
		Set<Authority> appAuthorities = authorities.stream().map(auth -> {
											String authStr = auth.getAuthority();
											return authorityRepository
													.findByAuthority(authStr);
									 	 })
										 .collect(Collectors.toSet());
		
		AppUser appUser = new AppUser();
		
		appUser.setUsername(user.getUsername());
		appUser.setPassword(user.getPassword());
		appUser.setAccountNonExpired(user.isAccountNonExpired());
		appUser.setAccountNonLocked(user.isAccountNonLocked());
		appUser.setEnabled(true);
		appUser.setAuthorities(appAuthorities);
		appUser.setCredentialsNonExpired(true);
		
		return appUser;
	}

	@Override
	public Integer createUser(AppUser appUser) throws UserExistsException {
		if(userExists(appUser.getUsername())) {
			throw new UserExistsException("User name exists");
		}
		appUser.setPassword(passwordEncoder.encode(appUser.getPassword()));
		appUser.setAccountNonExpired(true);
		appUser.setAccountNonLocked(true);
		appUser.setEnabled(true);
		appUser.setAuthorities(Set.of(new Authority(1, "ADMIN")));
		appUser.setCredentialsNonExpired(true);
		AppUser createdUser = userRepository.save(appUser);
		
		if(createdUser == null) {
			throw new InternalError("Error in creating the user");
		}
		return createdUser.getId();
	}

	@Override
	public void deleteUser(Integer userId) {
		userRepository.deleteById(userId);
	}

	@Override
	public void updateUser(AppUser appUser) {
		if(!userExists(appUser.getUsername()) && findByUserId(appUser.getId()) == null ) {
			throw new UsernameNotFoundException("User not found");
		}
		userRepository.save(appUser);
	}

	@Override
	public AppUser findByUserId(Integer id) {
		AppUser user = userRepository.findById(id)
									 .orElse(null);
		return user;
	}

	@Override
	public AppUser findByUserName(String name) {
		AppUser user = userRepository.findByUsername(name);
		return user;
	}
}
