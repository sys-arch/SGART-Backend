package com.team1.sgart.backend.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Column;
import java.util.Date;


@Entity
public class User {
    @Column
    private int id;

    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String lastName;
    
    @Id @Column(unique = true, nullable = false)
    private String email;

    
    private String department; //puede ser nulo
    
    @Column(nullable = false)
    private String center;
    
    @Column(nullable = false)
    private Date hiringDate;
    
    private String profile;//puede ser nulo
    
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private boolean blocked = false;
    
   
	public User(int id, String name, String lastName, String email, String department, String center, Date hiringDate,
			String profile, String password, boolean blocked) {
		this.id = id;
		this.name = name;
		this.lastName = lastName;
		this.email = email;
		this.department = department;
		this.center = center;
		this.hiringDate = hiringDate;
		this.profile = profile;
		this.password = password;
		this.blocked = blocked;
	}
	public User() {
		
	}
	
	// Getters y setters
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

	public Date getHiringDate() {
		return hiringDate;
	}

	public void setHiringDate(Date hiringDate) {
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


    
    
}

