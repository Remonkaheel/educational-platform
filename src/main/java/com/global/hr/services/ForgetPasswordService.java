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

    // تخزين OTP لكل مستخدم مع وقت انتهاء الصلاحية
    private Map<String, OtpDetails> otpStorage = new HashMap<>();

    private static final int MAX_ATTEMPTS = 3;
    // إرسال OTP إلى البريد
    public void sendOTP(String email) {
        String otp = generateOTP();
        LocalDateTime expiryTime = LocalDateTime.now().plusMinutes(5); // انتهاء الصلاحية بعد 5 دقائق

        otpStorage.put(email, new OtpDetails(otp, expiryTime));
        emailService.sendEmail(email, "Reset Password OTP", "Your OTP is: " + otp);
    }

    // التحقق من صحة OTP وانتهاء صلاحيته
    public boolean verifyOTP(String email, String otp) {
        OtpDetails details = otpStorage.get(email);

        if (details == null) {
            return false; // لا يوجد OTP لهذا البريد
        }
        if (details.getAttempts() >= MAX_ATTEMPTS) {
            otpStorage.remove(email);
            return false;
        }
        if (LocalDateTime.now().isAfter(details.getExpiryTime())) {
            otpStorage.remove(email); // حذف OTP من التخزين بعد انتهاء صلاحيته
            return false; // انتهت صلاحية الـ OTP
        }

        if (details.getOtp().equals(otp)) {
            otpStorage.remove(email); // حذف OTP بعد الاستخدام
            return true;
        }else {
            details.incrementAttempts(); // زيادة عدد المحاولات
            if (details.getAttempts() >= MAX_ATTEMPTS) {
                otpStorage.remove(email);
                return false;
            }
            return false;
        }
    }

    // إنشاء OTP عشوائي من 5 أرقام
    private String generateOTP() {
        Random random = new Random();
        int otp = 10000 + random.nextInt(90000);
        return String.valueOf(otp);
    }

    // كلاس داخلي لتخزين OTP مع وقت الانتهاء
    private static class OtpDetails {
        private String otp;
        private LocalDateTime expiryTime;
        private int attempts; // عدد المحاولات الفاشلة
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
