package com.team1.sgart.backend.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserDTO extends GenericUser{
	
	@JsonProperty("department")
	private String department;
	@JsonProperty("center")
	private String center;
	@JsonProperty("hiringDate")
	private String hiringDate;
	@JsonProperty("profile")
	private String profile;
	@JsonProperty("validated")
    private boolean validated = false;
	@JsonProperty("blocked")
	private boolean blocked;

	public UserDTO() {
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getCenter() {
		return center;
	}

	public void setCenter(String center) {
		this.center = center;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getHiringDate() {
		return hiringDate;
	}

	public void setHiringDate(String hiringDate) {
		this.hiringDate = hiringDate;
	}

	public String getProfile() {
		return profile;
	}

	public void setProfile(String profile) {
		this.profile = profile;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isBlocked() {
		return blocked;
	}

	public void setBlocked(boolean blocked) {
		this.blocked = blocked;
	}
	
	public boolean isValidated() {
		return validated;
	}
	
	public void setValidated(boolean validated) {
		this.validated = validated;
	}

}
