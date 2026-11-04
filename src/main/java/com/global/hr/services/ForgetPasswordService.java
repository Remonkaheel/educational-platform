package com.global.hr.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.HashMap;
import java.util.Map;

@Service
public class ForgetPasswordService {

    @Autowired
    private EmailService emailService;

    // Store an OTP for each user along with its expiry time
    private Map<String, OtpDetails> otpStorage = new HashMap<>();

    private static final int MAX_ATTEMPTS = 3;
    // Send the OTP to the email
    public void sendOTP(String email) {
        String otp = generateOTP();
        LocalDateTime expiryTime = LocalDateTime.now().plusMinutes(5); // Expires after 5 minutes

        otpStorage.put(email, new OtpDetails(otp, expiryTime));
        emailService.sendEmail(email, "Reset Password OTP", "Your OTP is: " + otp);
    }

    // Validate the OTP and its expiry
    public boolean verifyOTP(String email, String otp) {
        OtpDetails details = otpStorage.get(email);

        if (details == null) {
            return false; // No OTP exists for this email
        }
        if (details.getAttempts() >= MAX_ATTEMPTS) {
            otpStorage.remove(email);
            return false;
        }
        if (LocalDateTime.now().isAfter(details.getExpiryTime())) {
            otpStorage.remove(email); // Remove the OTP from storage after it expires
            return false; // The OTP has expired
        }

        if (details.getOtp().equals(otp)) {
            otpStorage.remove(email); // Remove the OTP after use
            return true;
        }else {
            details.incrementAttempts(); // Increase the number of attempts
            if (details.getAttempts() >= MAX_ATTEMPTS) {
                otpStorage.remove(email);
                return false;
            }
            return false;
        }
    }

    // Generate a random 5-digit OTP
    private String generateOTP() {
        Random random = new Random();
        int otp = 10000 + random.nextInt(90000);
        return String.valueOf(otp);
    }

    // Inner class to store the OTP along with its expiry time
    private static class OtpDetails {
        private String otp;
        private LocalDateTime expiryTime;
        private int attempts; // Number of failed attempts
        public OtpDetails(String otp, LocalDateTime expiryTime) {
            this.otp = otp;
            this.expiryTime = expiryTime;
        }

        public String getOtp() {
            return otp;
        }

        public LocalDateTime getExpiryTime() {
            return expiryTime;
        }
        public int getAttempts() {
            return attempts;
        }

        public void incrementAttempts() {
            this.attempts++;
        }
    }
}
