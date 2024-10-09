package com.team1.sgart.backend.model;

import javax.persistence.Column;
import javax.persistence.Id;

public abstract class GenericUser {

	@Column
	private int id;

	@Column(nullable = false)
	protected String name;
	@Column(nullable = false)
	protected String lastName;

	@Id
	@Column(unique = true, nullable = false)
	private String email;

	@Column(nullable = false)
	protected String password;

	public int getId() {
		return id;
	}

	public void setId(int id) {
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
