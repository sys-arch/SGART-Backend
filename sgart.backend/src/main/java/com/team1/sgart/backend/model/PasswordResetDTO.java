package com.team1.sgart.backend.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PasswordResetDTO {
	
	@JsonProperty("email")
	private String email;
	
	@JsonProperty("token")
	private String token;
	
	@JsonProperty("newPassword")
	private String newPassword;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
	
	

}
