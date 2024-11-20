package com.team1.sgart.backend.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "SGART_AdminsTable")
@AttributeOverrides({
	@AttributeOverride(name = "id", column = @Column(name = "admin_id", updatable = false, nullable = false)),
    @AttributeOverride(name = "name", column = @Column(name = "admin_name")),
    @AttributeOverride(name = "lastName", column = @Column(name = "admin_lastName")),
    @AttributeOverride(name = "email", column = @Column(name = "admin_email", unique = true)),
    @AttributeOverride(name = "password", column = @Column(name = "admin_password"))
})
public class Admin extends GenericUser {
	
	@Column(name = "admin_center")
	private String center;
	
	@JsonProperty("twoFactorAuthCode")
	@Column(name = "admin_twoFactorAuthCode")
    private String twoFactorAuthCode;
	
	@Column(name = "admin_blockedStatus", nullable = false)
	private boolean blocked;
	
	public Admin() {}

	
	public Admin(String name, String lastName, String email, String password) {
		super();
		this.name = name;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
	}
	
	public Admin(String name, String lastName, String email, String password, String center, String code) {
		super();
		this.name = name;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
		this.center = center;
		this.twoFactorAuthCode = code;
	}
    // Getters y setters


	public String getCenter() {
		return center;
	}
	
	public void setCenter(String center) {
		this.center=center;
	}
	
	public boolean getBlocked() {
		return blocked;
	}
}