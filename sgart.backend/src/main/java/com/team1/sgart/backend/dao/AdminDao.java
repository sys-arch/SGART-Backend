package com.team1.sgart.backend.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.team1.sgart.backend.model.Admin;

public interface AdminDao extends  JpaRepository<Admin, String> {
	    Optional<Admin> findByEmail(String email);
	    Optional<Admin> findByEmailAndPassword(String email, String password);
	    
	    @Query("SELECT a.twoFactorAuthCode FROM Admin a WHERE a.email = :email")
	    String obtenerAuthCodePorEmail(@Param("email") String email);
}
