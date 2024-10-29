package com.team1.sgart.backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

@Entity
@Table(name = "SGART_AdminsTable", indexes = @Index(columnList = "user_id", unique = true))
public class Admin extends GenericUser {

	public Admin() {
		super();
	}

    // Getters y setters
	
}