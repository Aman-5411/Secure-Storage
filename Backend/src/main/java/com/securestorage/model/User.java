package com.securestorage.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private String password;
    private String role;

    @Column(name = "is_2fa_enabled")
    private boolean is2faEnabled = false;
    @Column(name = "totp_secret")
    private String totpSecret;
    @Column(name = "reset_password_otp")
    private String resetPasswordOtp;

    @Column(name = "reset_password_otp_expiry")
    private LocalDateTime resetPasswordOtpExpiry;
    // Constructors
    public User() {}
    public User(Long id,String name, String email,String password,String role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    // Getters / Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public boolean is2faEnabled() { return is2faEnabled; }
    public void set2faEnabled(boolean is2faEnabled) {this.is2faEnabled = is2faEnabled;}

    public String getTotpSecret() {return totpSecret;}
    public void setTotpSecret(String totpSecret) {this.totpSecret = totpSecret;}

    public String getResetPasswordOtp() {return resetPasswordOtp;}
    public void setResetPasswordOtp(String resetPasswordOtp) {this.resetPasswordOtp = resetPasswordOtp;}

    public LocalDateTime getResetPasswordOtpExpiry() {return resetPasswordOtpExpiry;}
    public void setResetPasswordOtpExpiry(LocalDateTime resetPasswordOtpExpiry) {this.resetPasswordOtpExpiry = resetPasswordOtpExpiry;}
}
