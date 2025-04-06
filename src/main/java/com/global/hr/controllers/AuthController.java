package com.global.hr.controllers;

import com.global.hr.config.jwt.JwtUtil;
import com.global.hr.models.*;
import com.global.hr.models.DTO.*;
import com.global.hr.repositories.UserRepository;
import com.global.hr.services.EmailService;
import com.global.hr.services.ForgetPasswordService;
import com.global.hr.services.UserService;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ForgetPasswordService forgetPasswordService;
    
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }
    @PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> register(
            @RequestParam("fullName") String fullName,
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            @RequestParam("role") String role,
            @RequestParam(value = "profilePicture", required = false) MultipartFile profilePicture,
            @RequestParam(value = "phoneNumber") String phoneNumber,
            @RequestParam(value = "dateOfBirth") LocalDate dateOfBirth,
            @RequestParam(value = "description") String description,
            @RequestParam(value = "professionalTitle", required = false) String professionalTitle,
            @RequestParam(value = "expertsInterests") List<String> expertsInterests,
            @RequestParam(value = "personalLinks", required = false) List<String> personalLinks) throws IOException {

    	 RegisterDTO registerDTO = new RegisterDTO(fullName, email, password, role, profilePicture, phoneNumber,
    	            dateOfBirth, description, professionalTitle, expertsInterests, personalLinks);

        User result = userService.registerUser(registerDTO);

        if (result == null) {
            return ResponseEntity.badRequest().body("Email is already registered.");
        }

        User savedUser = userService.getUserByEmail(registerDTO.getEmail());

        if (savedUser == null) {
            return ResponseEntity.internalServerError().body("Registration failed.");
        }
        String token = jwtUtil.generateToken(email);

        ProfileResponse response = new ProfileResponse(result, token);
        return ResponseEntity.ok(response);
    
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@Validated @RequestBody LoginDTO loginDTO) {
    	Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword()));
    	  User user = userService.getUserByEmail(loginDTO.getEmail());
    	    if (user == null) {
    	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials.");
    	    }
    	    String token = jwtUtil.generateToken(user.getEmail());
    	    ProfileResponse response = new ProfileResponse(user, token);

    	    return ResponseEntity.ok(response);
    	}
    
    
    
    
    
    
    
 // ✅ 1️⃣ إرسال OTP للإيميل
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordDTO request) {
        forgetPasswordService.sendOTP(request.getEmail());
        return ResponseEntity.ok("OTP sent successfully to your email.");
    }

    // ✅ 2️⃣ التحقق من OTP
    @PostMapping("/verify-otp")
    public ResponseEntity<Boolean> verifyOTP(@RequestParam String email, @RequestParam String otp) {
        boolean isValid = forgetPasswordService.verifyOTP(email, otp);
        return ResponseEntity.ok(isValid);
    }


    
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestParam String email, @RequestParam String newPassword) {
        User user = userService.getUserByEmail(email);
        if (user == null) {
            return ResponseEntity.badRequest().body("User not found.");
        }

        // تحديث كلمة المرور
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        return ResponseEntity.ok("Password reset successfully.");
}
}
    