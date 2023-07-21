package com.auth.model;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

public class PublicUserDetails {

	@NonNull 
	private String userName; 
	
	@Nullable 
	private Integer id;
	
	@NonNull 
	private String password;
	
	@Nullable 
	@DateTimeFormat(iso = ISO.DATE) 
	private Date dob;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

	@Override
	public String toString() {
		return "PublicUserDetails [userName=" + userName + ", id=" + id + ", password=" + password + ", dob=" + dob
				+ "]";
	}
}
