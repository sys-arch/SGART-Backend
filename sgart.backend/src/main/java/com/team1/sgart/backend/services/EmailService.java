package com.team1.sgart.backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private JavaMailSender mailSender;  // Configura el mail sender de Spring Boot
    
    @Autowired
	public EmailService(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}

    public void sendPasswordResetEmail(String destinatary, String recoveryLink) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(destinatary);
        message.setSubject("Recuperación de Contraseña");
        message.setText("Haz clic en el siguiente enlace para restablecer tu contraseña: " + recoveryLink + "\n\nEste enlace expirará en 15 minutos.");
        mailSender.send(message);
    }
}
