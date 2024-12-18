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
            .cors(cors -> cors.configure(http))
            .authorizeHttpRequests(auth -> auth
            		.requestMatchers("/auth/forgot-password").permitAll() 
                    .requestMatchers("/administrador/**").authenticated()
                	.requestMatchers("/administrador/calendarios/invitados").authenticated()
                	.requestMatchers("/administrador/calendarios/**").authenticated()
                	.requestMatchers("/auth/validate-totp").permitAll() 
                    .requestMatchers("/auth/generate-qr").permitAll() // Permitir acceso sin autenticación
                    .requestMatchers("/users/current/userId").authenticated() // Permitir acceso a cualquier usuario autenticado
                    .requestMatchers("/api/meetings/**").authenticated() // Restringir acceso a empleados
                    .requestMatchers("/auth/validate-token").permitAll() // Permitir acceso sin autenticación
                    .requestMatchers("/invitations/**").hasAuthority("employee") // Solo empleados pueden invitar
                    .requestMatchers("/admin/**").hasAuthority("admin")
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
