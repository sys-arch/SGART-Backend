package com.team1.sgart.backend.config;

import com.team1.sgart.backend.util.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Deshabilitar CSRF
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/auth/validate-token").permitAll() // Permitir acceso sin autenticación
                .requestMatchers("/admin/**").hasAuthority("admin") // Solo admin puede acceder
                .requestMatchers("/employee/**").hasAuthority("employee") // Solo empleados pueden acceder
                .requestMatchers("/users/**").permitAll() // Solo usuarios pueden acceder
                .anyRequest().authenticated() // Cualquier otra solicitud requiere autenticación
            )
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class); // Añadir filtro JWT

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}

