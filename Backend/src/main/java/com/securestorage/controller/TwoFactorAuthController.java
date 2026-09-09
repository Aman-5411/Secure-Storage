package com.securestorage.controller;

import com.securestorage.model.User;
import com.securestorage.repository.UserRepository;
import com.securestorage.service.TwoFactorAuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/2fa")
public class TwoFactorAuthController {

    private final TwoFactorAuthService tfaService;
    private final UserRepository userRepository;

    public TwoFactorAuthController(TwoFactorAuthService tfaService, UserRepository userRepository) {
        this.tfaService = tfaService;
        this.userRepository = userRepository;
    }

    /**
     * Endpoint to start the 2FA setup process.
     * Generates a new secret and a QR code for the user to scan.
     */
    @PostMapping("/setup")
    public ResponseEntity<?> setup2FA(Authentication authentication) {
        // Get the currently authenticated user
        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Generate a new 2FA secret
        String secret = tfaService.generateNewSecret();

        // Save the secret to the user (but don't enable 2FA yet)
        user.setTotpSecret(secret);
        user.set2faEnabled(false); // It's only enabled *after* they verify
        userRepository.save(user);

        // Generate the QR code
        String qrCodeUrlData = tfaService.getTotpQrCodeUrl(secret, user.getEmail(), "SecureStorageApp");
        String qrCodeImageBase64 = tfaService.generateQrCodeImageBase64(qrCodeUrlData);

        // Return the Base64 image so the frontend can display it
        // (e.g., <img src="data:image/png;base64, ...[qrCodeImageBase64]...">)
        return ResponseEntity.ok(Map.of("qrCodeImageBase64", qrCodeImageBase64));
    }

    /**
     * Endpoint to verify the 6-digit code and enable 2FA.
     * The user calls this *after* scanning the QR code.
     */
    @PostMapping("/verify")
    public ResponseEntity<?> verify2FA(Authentication authentication,
                                       @RequestBody Map<String, String> request) {

        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Get the 6-digit code from the request body
        int code;
        try {
            code = Integer.parseInt(request.get("code"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Invalid code format. Must be a 6-digit number.");
        }

        // Validate the code against the user's saved secret
        if (tfaService.isCodeValid(user.getTotpSecret(), code)) {
            // Code is valid! Enable 2FA for the user.
            user.set2faEnabled(true);
            userRepository.save(user);
            return ResponseEntity.ok(Map.of("message", "2FA enabled successfully"));
        } else {
            // Code is invalid
            return ResponseEntity.badRequest().body("Invalid 2FA code. Please try again.");
        }
    }
}