package com.radioaudit.endpoint.model;

import java.io.Serializable;

public class UserAuthenticationModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String email;
	private String password;
	private boolean rememberMe;

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean isRememberMe() {
		return rememberMe;
	}

	public void setRememberMe(boolean rememberMe) {
		this.rememberMe = rememberMe;
	}

}
