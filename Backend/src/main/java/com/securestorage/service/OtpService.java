package com.securestorage.service;

import com.securestorage.exception.InvalidOtpException;
import com.securestorage.model.User;
import com.securestorage.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Service
public class OtpService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // We'll set the OTP to expire in 10 minutes
    private static final int OTP_EXPIRY_MINUTES = 10;

    /**
     * Generates a 6-digit OTP, saves it to the user, and sends it via email.
     * This is used for "Forgot Password".
     */
    @Transactional
    public void generateAndSendOtp(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));

        String otp = generateNumericOtp(6);

        // Set the OTP and its expiry time on the user object
        user.setResetPasswordOtp(otp);
        user.setResetPasswordOtpExpiry(LocalDateTime.now().plusMinutes(OTP_EXPIRY_MINUTES));

        userRepository.save(user);

        // Send the email
        emailService.sendOtpEmail(email, "Your Password Reset OTP", otp);
    }

    /**
     * Validates the provided OTP and resets the user's password.
     */
    public void validateOtpAndResetPassword(String email, String otp, String newPassword) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));

        // 1. Check if OTP is correct
        if (user.getResetPasswordOtp() == null || !user.getResetPasswordOtp().equals(otp)) {
            throw new InvalidOtpException("Invalid OTP.");
        }

        // 2. Check if OTP is expired
        if (user.getResetPasswordOtpExpiry() == null || user.getResetPasswordOtpExpiry().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("OTP has expired.");
        }

        // 3. All checks passed. Reset the password.
        user.setPassword(passwordEncoder.encode(newPassword));

        // 4. Invalidate the OTP so it can't be used again
        user.setResetPasswordOtp(null);
        user.setResetPasswordOtpExpiry(null);

        userRepository.save(user);
    }

    /**
     * Generates a random N-digit numeric OTP.
     */
    private String generateNumericOtp(int length) {
        SecureRandom random = new SecureRandom();
        int otpValue = 100000 + random.nextInt(900000);

        return String.valueOf(otpValue);
    }
}