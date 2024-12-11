package com.team1.sgart.backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Column;
import java.util.regex.Pattern;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "SGART_UsersTable", indexes = @Index(columnList = "user_id", unique = true))
public class User extends GenericUser{
	
	@Column(name = "user_department")
	private String department;
	
	@Column(name = "user_center", nullable = false)
	private String center;
	
	@Column(name = "user_hiringDate", nullable = false)
	private String hiringDate;
	
	@Column(name = "user_profile")
	private String profile;

	@Transient
	private String passwordConfirm;
  
    @Column(name = "user_validatedStatus", nullable = false)
    private boolean validated = false;
	
    @Column(name = "user_blockedStatus", nullable = false)
	private boolean blocked;
    
    @JsonProperty("twoFactorAuthCode")
    @Column(name = "user_twoFactorAuthCode", nullable = false)
    private String twoFactorAuthCode;

	public User(String email, String name, String lastName, String department, String center, String hiringDate,
			String profile, String password, String passwordConfirm, boolean blocked, boolean validated, String twoFactorAuthCode) {
		this.email=email;
		this.name = name;
		this.lastName = lastName;
		this.department = department;
		this.center = center;
		this.hiringDate = hiringDate;
		this.profile = profile;
		this.password = password;
		this.passwordConfirm = passwordConfirm;
		this.blocked = blocked;
		this.validated = validated;
		this.twoFactorAuthCode = twoFactorAuthCode;
	}

	public User() {
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

	public String getPasswordConfirm() {
		return passwordConfirm;
	}

	public void setPasswordConfirm(String passwordConfirm) {
		this.passwordConfirm = passwordConfirm;
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

	

	public boolean comprobarFormatoEmail() {
		boolean valido = true;
		String emailPattern = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
		if (!Pattern.matches(emailPattern, email)) {
			valido = false;
		}
		return valido;
	}

	public boolean comprobarFormatoFecha() {
        boolean valido = true;
        String fechaPattern = "^\\d{2}/\\d{2}/\\d{4}$";
		if (!Pattern.matches(fechaPattern, hiringDate)) {
			valido = false;
		}
        return valido;
    }

}
