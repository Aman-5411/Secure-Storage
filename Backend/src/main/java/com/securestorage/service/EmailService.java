package com.securestorage.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    // Get the 'from' address from properties, or use a default
    @Value("${spring.mail.username:noreply@securestorage.com}")
    private String fromEmail;

    /**
     * Sends a simple email with an OTP.
     * @param toEmail The recipient's email address.
     * @param subject The subject line of the email.
     * @param otp The 6-digit one-time password.
     */
    public void sendOtpEmail(String toEmail, String subject, String otp) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject(subject);

            // This is the email body
            String emailText = String.format(
                    "Hello,\n\nYour One-Time Password (OTP) for %s is:\n\n%s\n\nThis code is valid for 10 minutes.\n\nIf you did not request this, please ignore this email.",
                    subject,
                    otp
            );
            message.setText(emailText);

            mailSender.send(message);

        } catch (Exception e) {
            // In a real app, you should log this error
            throw new RuntimeException("Error while sending OTP email: " + e.getMessage(), e);
        }
    }
}