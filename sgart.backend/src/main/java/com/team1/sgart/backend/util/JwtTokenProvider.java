package com.team1.sgart.backend.util;

import java.nio.charset.StandardCharsets;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import com.team1.sgart.backend.model.User;
import com.team1.sgart.backend.model.GenericUser;
import com.team1.sgart.backend.model.Admin;



import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;

@Component
public class JwtTokenProvider {

	    private SecretKey key;

	    @Value("${jwt.secret}")
	    private String secretKey;

	    private static final long EXPIRATION_TIME = ((15 * 60) * 1000L); // 15 minutos

	    @PostConstruct
	    public void init() {
	        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
	        this.key = Keys.hmacShaKeyFor(keyBytes);
	    }

	    public String generateToken(GenericUser user) {
	        String role = user instanceof Admin ? "admin" : "employee";
	        return Jwts.builder()
	                .setSubject(user.getEmail())
	                .claim("role", role)
	                .claim("userId", user.getID().toString()) // Incluye el userId en los claims
	                .setIssuedAt(new Date())
	                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
	                .signWith(key)
	                .compact();
	    }

	    public String generatePasswordResetToken(User user) {
	        return Jwts.builder()
	                .setSubject(user.getEmail())
	                .setIssuedAt(new Date())
	                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
	                .signWith(key)
	                .compact();
	    }


	    public boolean validateToken(String token) {
	        try {
	            Jwts.parserBuilder()
	                .setSigningKey(key)
	                .build()
	                .parseClaimsJws(token);
	            return true;
	        } catch (Exception e) {
	            return false;
	        }
	    }

	    public String getEmailFromToken(String token) {
	        Claims claims = Jwts.parserBuilder()
	                .setSigningKey(key)
	                .build()
	                .parseClaimsJws(token)
	                .getBody();
	        return claims.getSubject();
	    }

	    public String getRoleFromToken(String token) {
	        Claims claims = Jwts.parserBuilder()
	                .setSigningKey(key)
	                .build()
	                .parseClaimsJws(token)
	                .getBody();
	        return claims.get("role", String.class);
	    }
	    
	    public String getUserIdFromToken(String token) {
	        try {
	            Claims claims = Jwts.parserBuilder()
	                    .setSigningKey(key) // Clave secreta configurada
	                    .build()
	                    .parseClaimsJws(token)
	                    .getBody();
	            return claims.get("userId", String.class); // Asegúrate de incluir `userId` como un claim en el JWT
	        } catch (Exception e) {
	            return null; // Retorna null si el token no es válido
	        }
	    }


	}
