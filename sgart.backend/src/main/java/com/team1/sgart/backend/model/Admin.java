package com.team1.sgart.backend.model;

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
	
	@Column(name = "admin_twoFactorAuthCode", nullable = false)
    private String twoFactorAuthCode;

	
	public Admin(String name, String lastName, String email, String password) {
		super();
		this.name = name;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
		
	}
    // Getters y setters
	
}