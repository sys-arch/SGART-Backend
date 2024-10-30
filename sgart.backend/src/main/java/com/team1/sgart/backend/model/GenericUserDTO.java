package com.team1.sgart.backend.model;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class GenericUserDTO {

	@JsonProperty("id")
	protected UUID id;
	@JsonProperty("name")
	protected String name;
	@JsonProperty("lastName")
	protected String lastName;
	@JsonProperty("email")
	protected String email;
	@JsonProperty("password")
	protected String password;
	
	public UUID getID() {
		return id;
	}
	
	public void setID(UUID uuid) {
		this.id = uuid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	// Getters y setters
}

