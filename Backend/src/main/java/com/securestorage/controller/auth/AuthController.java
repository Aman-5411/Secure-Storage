package com.securestorage.controller.auth;

import com.securestorage.dto.UserRequestDTO;
import com.securestorage.model.User;
import com.securestorage.repository.UserRepository;
import com.securestorage.security.JwtUtil;
import org.springframework.security.core.userdetails.UserDetails;
import com.securestorage.service.TwoFactorAuthService;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.securestorage.service.OtpService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final TwoFactorAuthService tfaService;
    private final OtpService otpService;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil, TwoFactorAuthService tfaService,OtpService otpService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.tfaService = tfaService;
        this.otpService = otpService;
    }

    // Register user
    @PostMapping("/register")
    public String register(@RequestBody UserRequestDTO request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            return "User already exists with email: " + request.getEmail();
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        String role = request.getRole();
        if (role == null || role.isBlank()) {
            role = "ROLE_USER";
        }
        user.setRole(role.toUpperCase());

        userRepository.save(user);
        return "User registered successfully with role: " + role;
    }

    // Login user
    @PostMapping("/login")
    public Map<String, String> login(@RequestBody UserRequestDTO request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        if (user.is2faEnabled()) {
            int code = request.getTotpCode(); // Get code from DTO

            if (code == 0 || !tfaService.isCodeValid(user.getTotpSecret(), code)) {
                return Map.of(
                        "status", "2FA_REQUIRED",
                        "email", user.getEmail()
                );
            }
        }
        // Generate a UserDetails object to pass to JwtUtil
        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRole().replace("ROLE_", ""))
                .build();

        // Generate JWT token with role
        String token = jwtUtil.generateToken(userDetails, user.getRole());

        return Map.of(
                "token", token,
                "role", user.getRole(),
                "email", user.getEmail()
        );
    }
    // Endpoint : Forgot Password
    @PostMapping("/forgot-password")
    public Map<String, String> forgotPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        if (email == null || email.isBlank()) {
            throw new RuntimeException("Email is required.");
        }
        otpService.generateAndSendOtp(email);
        return Map.of("message", "OTP sent to your email.");
    }

    // ENDPOINT: RESET PASSWORD
    @PostMapping("/reset-password")
    public Map<String, String> resetPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String otp = request.get("otp");
        String newPassword = request.get("newPassword");

        if(email == null) {
            throw new RuntimeException("Email is required.");
        }

        if(otp == null) {
            throw new RuntimeException("OTP is required.");
        }

        if(newPassword == null) {
            throw new RuntimeException("New password is required.");
        }

        otpService.validateOtpAndResetPassword(email, otp, newPassword);
        return Map.of("message", "Password reset successfully.");
    }
}
