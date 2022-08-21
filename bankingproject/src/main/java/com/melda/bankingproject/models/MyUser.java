package com.melda.bankingproject.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class MyUser {
	private int id;
	private String username;
	private String email;
	@JsonIgnore
	private String password;
	private boolean enabled;
	private String authorities;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isEnabled() {
		return this.enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getAuthorities() {
		return this.authorities;
	}

	public void setAuthorities(String authorities) {
		this.authorities = authorities;
	}
}
