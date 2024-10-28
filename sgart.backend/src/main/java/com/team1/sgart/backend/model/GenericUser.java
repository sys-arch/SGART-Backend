package com.team1.sgart.backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class GenericUser {

	@Id
	@Column(name = "user_id")
	protected String id;

	@Column(name = "user_name", nullable = false)
	protected String name;
	
	@Column(name = "user_lastName", nullable = false)
	protected String lastName;

	@Column(name = "user_email", unique = true, nullable = false)
	protected String email;

	@Column(name = "user_password", nullable = false)
	protected String password;
	
	public String getID() {
		return id;
	}
	
	public void setID(String id) {
		this.id = id;
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
