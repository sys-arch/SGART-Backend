package com.team1.sgart.backend.services;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.springframework.stereotype.Service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.team1.sgart.backend.dao.UserDao;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;


@Service
public class TwoFactorAuthService {

    private final GoogleAuthenticator gAuth = new GoogleAuthenticator();

    // Método para generar la clave secreta que usará Google Authenticator
    public String generateSecretKey() {
        GoogleAuthenticatorKey key = gAuth.createCredentials();
        return key.getKey(); // Devuelve la clave secreta en formato Base32
    }

   public String getQRCodeURL(String username, String secretKey) {
        return "otpauth://totp/" + username + "?secret=" + secretKey + "&issuer=ACMECo";
    }

    // Método para generar el QR en formato byte[] 
    public byte[] generateQRCodeImage(String username, String secretKey) throws WriterException, IOException {
        String qrCodeText = getQRCodeURL(username, secretKey);
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(qrCodeText, BarcodeFormat.QR_CODE, 200, 200);

        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        BufferedImage qrImage = MatrixToImageWriter.toBufferedImage(bitMatrix);
        ImageIO.write(qrImage, "PNG", pngOutputStream);
        return pngOutputStream.toByteArray();
    }

    
    public boolean validateTOTP(String secretKey, String code) {
        return gAuth.authorize(secretKey, Integer.parseInt(code));
    }

    public boolean validateTOTPFromDB(String email, String code) {
        String secretKey = UserDao.obtenerAuthCodePorEmail(email);
        if (secretKey == null) {
            throw new IllegalArgumentException("Secret key not found for user: " + email);
        }

        return gAuth.authorize(secretKey, Integer.parseInt(code));
    }
}