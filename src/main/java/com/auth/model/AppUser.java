package com.auth.model;

import java.util.Date;
import java.util.Set;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.NonNull;

import com.auth.common.model.CommonUserDetails;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "user")
public class AppUser {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@NonNull
	@Column(unique = true)
	private String username;
	
	@NonNull
	private String password;
	
	@Column(name = "first_name")
	private String firstName;
	
	@Column(name = "last_name")
	private String lastName;
	
	@ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
	@JoinTable(name = "user_authorities", joinColumns = {
			@JoinColumn(name = "USERS_ID", referencedColumnName = "ID") }, inverseJoinColumns = {
					@JoinColumn(name = "AUTHORITIES_ID", referencedColumnName = "ID") })
	private Set<Authority> authorities;
		
	@DateTimeFormat(pattern = "dd-mm-yyyy")
	private Date dob;
	
	private String mobile;
	
	@NonNull
	private String email;
	
	@Enumerated(EnumType.STRING)
	private AuthProvider provider;
	
	private String providerId;
	
	private Boolean accountNonExpired;
	private Boolean accountNonLocked;
	private Boolean credentialsNonExpired;
	private Boolean enabled;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Set<Authority> getAuthorities() {
		return authorities;
	}
	public void setAuthorities(Set<Authority> authorities) {
		this.authorities = authorities;
	}
	public Boolean getAccountNonExpired() {
		return accountNonExpired;
	}
	public void setAccountNonExpired(Boolean accountNonExpired) {
		this.accountNonExpired = accountNonExpired;
	}
	public Boolean getAccountNonLocked() {
		return accountNonLocked;
	}
	public void setAccountNonLocked(Boolean accountNonLocked) {
		this.accountNonLocked = accountNonLocked;
	}
	public Boolean getCredentialsNonExpired() {
		return credentialsNonExpired;
	}
	public void setCredentialsNonExpired(Boolean credentialsNonExpired) {
		this.credentialsNonExpired = credentialsNonExpired;
	}
	public Boolean getEnabled() {
		return enabled;
	}
	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}
	
	public Date getDob() {
		return dob;
	}
	public void setDob(Date dob) {
		this.dob = dob;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	
	
	public AuthProvider getProvider() {
		return provider;
	}
	public void setProvider(AuthProvider provider) {
		this.provider = provider;
	}
	public String getProviderId() {
		return providerId;
	}
	public void setProviderId(String providerId) {
		this.providerId = providerId;
	}
	
	public AppUser(Integer id, String username, String password, String firstName, String lastName,
			Set<Authority> authorities, Date dob, String mobile, String email, Boolean accountNonExpired,
			Boolean accountNonLocked, Boolean credentialsNonExpired, Boolean enabled) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.authorities = authorities;
		this.dob = dob;
		this.mobile = mobile;
		this.email = email;
		this.accountNonExpired = accountNonExpired;
		this.accountNonLocked = accountNonLocked;
		this.credentialsNonExpired = credentialsNonExpired;
		this.enabled = enabled;
	}
	public AppUser() {}
	
	public static AppUser toAppUser(CommonUserDetails commonUserDetails) {
		AppUser appUser = new AppUser();
		appUser.setId(commonUserDetails.getId());
		appUser.setUsername(commonUserDetails.getUserName());
		appUser.setPassword(commonUserDetails.getPassword());
		appUser.setAuthorities(null);
		appUser.setAccountNonExpired(true);
		appUser.setAccountNonLocked(true);
		appUser.setCredentialsNonExpired(true);
		appUser.setEnabled(true);
		appUser.setDob(commonUserDetails.getDob());
		appUser.setEmail(commonUserDetails.getEmail());
		appUser.setFirstName(commonUserDetails.getFirstName());
		appUser.setLastName(commonUserDetails.getLastName());
		appUser.setMobile(commonUserDetails.getMobile());
		
		return appUser;
	}
	public static CommonUserDetails toCommonUserDetails(AppUser appUser) {
		CommonUserDetails commonUserDetails = new CommonUserDetails();
		commonUserDetails.setId(appUser.getId());
		commonUserDetails.setUserName(appUser.getUsername());
		commonUserDetails.setPassword(appUser.getPassword());
		commonUserDetails.setDob(appUser.getDob());
		commonUserDetails.setFirstName(appUser.getFirstName());
		commonUserDetails.setLastName(appUser.getLastName());
		commonUserDetails.setEmail(appUser.getEmail());
		commonUserDetails.setMobile(appUser.getMobile());
		
		return commonUserDetails;
	}
}
