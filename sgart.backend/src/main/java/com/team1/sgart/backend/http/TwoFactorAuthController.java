package com.team1.sgart.backend.http;

import java.util.Base64;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.team1.sgart.backend.model.PasswordResetDTO;
import com.team1.sgart.backend.model.TwoFactorAuth;
import com.team1.sgart.backend.services.EmailService;
import com.team1.sgart.backend.services.TwoFactorAuthService;
import com.team1.sgart.backend.services.UserService;
import com.team1.sgart.backend.util.JwtTokenProvider;

@RestController
@RequestMapping("/auth")

public class TwoFactorAuthController {
	
	private UserService userService;
    private TwoFactorAuthService twoFactorAuthService;
    private EmailService emailService;
    private JwtTokenProvider jwtTokenProvider;
    
    @Autowired
	public TwoFactorAuthController(TwoFactorAuthService twoFactorAuthService, EmailService emailService, UserService userService, JwtTokenProvider jwtTokenProvider) {
		this.twoFactorAuthService = twoFactorAuthService;
		this.emailService = emailService;
		this.userService = userService;
		this.jwtTokenProvider = jwtTokenProvider;
	}

    // ConcurrentHashMap para almacenar las claves secretas temporalmente
    private ConcurrentHashMap<String, String> secretKeyStore = new ConcurrentHashMap<>();

    @GetMapping("/generate-qr")
    public ResponseEntity<String> generateQRCode(@RequestParam String email) {
        try {
            String secretKey = twoFactorAuthService.generateSecretKey(); // Genera la clave secreta
            byte[] qrCodeImage = twoFactorAuthService.generateQRCodeImage(email, secretKey);
            String base64QRCode = Base64.getEncoder().encodeToString(qrCodeImage);

            // Almacenar la clave secreta en el ConcurrentHashMap
            secretKeyStore.put(email, secretKey);

            // Devuelve la clave secreta también si es necesario almacenar
            return ResponseEntity.ok("{\"qrCode\": \"" + base64QRCode + "\", \"secretKey\": \"" + secretKey + "\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("{\"error\": \"Error al generar el código QR\"}");
        }
    }

    @PostMapping("/validate-totp")
    public ResponseEntity<String> validateTOTP(@RequestBody TwoFactorAuth request) {
        try {
            // Obtener la clave secreta del ConcurrentHashMap
            String secretKey = secretKeyStore.get(request.getMail());
            if (secretKey == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                     .body("{\"status\": \"invalid\", \"error\": \"Secret key not found\"}");
            }

            boolean isValid = twoFactorAuthService.validateTOTP(secretKey, request.getCode());
            if (isValid) {
                // Eliminar la clave secreta del ConcurrentHashMap después de la validación
                secretKeyStore.remove(request.getMail());
                return ResponseEntity.ok("{\"status\": \"valid\"}");
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                     .body("{\"status\": \"invalid\"}");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("{\"error\": \"Error al validar el código TOTP\"}");
        }
    }

    @PostMapping("/validate-totp-db")
    public ResponseEntity<String> validateTOTPFromDB(@RequestBody TwoFactorAuth request) {
        try {
            boolean isValid = twoFactorAuthService.validateTOTPFromDB(request.getMail(), request.getCode());
            if (isValid) {
                return ResponseEntity.ok("{\"status\": \"valid\"}");
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                     .body("{\"status\": \"invalid\"}");
            }
        } catch (Exception e) {
        	System.err.println("Error: " + e.getClass().getName() + " - " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("{\"error\": \"Error al validar el código TOTP\"}");
        }
    }
    
    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody Map<String, String> emailData) {
        try {
            String email = emailData.get("email");
            if (email == null || email.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                    .body("{\"error\": \"El email es requerido\"}");
            }

            // Validar que el usuario existe y generar token
            String token = userService.generatePasswordResetToken(email);
            
            
            // Construir el link de recuperación
            String recoveryLink = "https://sgart-v1.web.app/#/reset-password?token=" + token;
            
            // Enviar el email con el link
            emailService.sendPasswordResetEmail(email, recoveryLink);
            
            return ResponseEntity.ok()
                .body("{\"message\": \"Se ha enviado un enlace de recuperación a tu correo\", \"email\": \"" + email + "\"}");
                
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode())
                .body("{\"error\": \"" + e.getReason() + "\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("{\"error\": \"Error al procesar la solicitud: " + e.getMessage() + "\"}");
        }
    }
    
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody PasswordResetDTO resetRequest) {
        try {
            // Validar que tenemos todos los datos necesarios
            if (resetRequest.getToken() == null || resetRequest.getToken().trim().isEmpty()) {
                return ResponseEntity.badRequest()
                    .body("{\"error\": \"El token es requerido\"}");
            }
            
            if (resetRequest.getNewPassword() == null || resetRequest.getNewPassword().trim().isEmpty()) {
                return ResponseEntity.badRequest()
                    .body("{\"error\": \"La nueva contraseña es requerida\"}");
            }

            // Intentar restablecer la contraseña
            userService.resetPassword(resetRequest.getToken(), resetRequest.getNewPassword());
            
            return ResponseEntity.ok()
                .body("{\"message\": \"Contraseña actualizada correctamente\"}");
                
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode())
                .body("{\"error\": \"" + e.getReason() + "\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("{\"error\": \"Error al restablecer la contraseña: " + e.getMessage() + "\"}");
        }
    }

}