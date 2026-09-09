package com.securestorage.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import com.warrenstrange.googleauth.GoogleAuthenticatorQRGenerator;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.Base64;

@Service
public class TwoFactorAuthService {

    private final GoogleAuthenticator gAuth;

    public TwoFactorAuthService() {
        this.gAuth = new GoogleAuthenticator();
    }

    /**
     * Generates a new, random 2FA secret key.
     * @return The Base32 encoded secret key.
     */
    public String generateNewSecret() {
        GoogleAuthenticatorKey key = gAuth.createCredentials();
        return key.getKey();
    }

    /**
     * Generates the standard TOTP URL needed for a QR code.
     * @param secret The user's secret key
     * @param email The user's email
     * @param issuer A name for your application (e.g., "SecureStorageApp")
     * @return The otpauth:// URL string
     */
    public String getTotpQrCodeUrl(String secret, String email, String issuer) {
        // Format: otpauth://totp/ISSUER:EMAIL?secret=SECRET&issuer=ISSUER
        return String.format(
                "otpauth://totp/%s:%s?secret=%s&issuer=%s",
                issuer, email, secret, issuer
        );
    }

    /**
     * Generates a Base64-encoded PNG image of the QR code.
     * A frontend can display this directly using: <img src="data:image/png;base64,...">
     * @param qrCodeData The otpauth:// URL from getTotpQrCodeUrl()
     * @return A Base64 string of the PNG image
     */
    public String generateQrCodeImageBase64(String qrCodeData) {
        try {
            // the QR code matrix
            BitMatrix matrix = new MultiFormatWriter().encode(qrCodeData, BarcodeFormat.QR_CODE, 200, 200);

            // Write the matrix to a byte stream as a PNG
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(matrix, "PNG", bos);

            // Encode the byte stream to Base64
            return Base64.getEncoder().encodeToString(bos.toByteArray());
        } catch (Exception e) {
            // This should not happen with valid data
            throw new RuntimeException("Could not generate QR code image", e);
        }
    }

    /**
     * Validates a 6-digit TOTP code against a user's secret.
     * @param secret The user's secret key
     * @param code The 6-digit code (as an integer)
     * @return true if the code is valid, false otherwise
     */
    public boolean isCodeValid(String secret, int code) {
        // The library handles all the time-based logic
        return gAuth.authorize(secret, code);
    }
}