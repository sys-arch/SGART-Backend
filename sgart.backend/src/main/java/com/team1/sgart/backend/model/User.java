package com.team1.sgart.backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import java.util.Date;


@Entity
public class User extends GenericUser {
       
    private String department; //puede ser nulo
    
    @Column(nullable = false)
    private String center;
    
    @Column(nullable = false)
    private Date hiringDate;
    
    private String profile;//puede ser nulo
    
    @Column(nullable = false)
    private boolean blocked = false;
    
   

	public User(String department, String center, Date hiringDate, String profile, boolean blocked) {
		super();
		this.department = department;
		this.center = center;
		this.hiringDate = hiringDate;
		this.profile = profile;
		this.blocked = blocked;
	}

	public User() {
		
	}
	
	// Getters y setters

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

	public boolean isBlocked() {
		return blocked;
	}

	public void setBlocked(boolean blocked) {
		this.blocked = blocked;
	}


    
    
}

