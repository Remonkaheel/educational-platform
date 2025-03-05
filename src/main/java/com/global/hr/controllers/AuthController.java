package com.global.hr.controllers;

import com.global.hr.config.jwt.JwtUtil;
import com.global.hr.models.*;
import com.global.hr.models.DTO.*;
import com.global.hr.repositories.UserRepository;
import com.global.hr.services.EmailService;
import com.global.hr.services.ForgetPasswordService;
import com.global.hr.services.UserService;

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
            @RequestParam(value = "personalLinks", required = false) List<String> personalLinks) {

        RegisterDTO registerDTO = new RegisterDTO();
        registerDTO.setFullName(fullName);
        registerDTO.setEmail(email);
        registerDTO.setPassword(password);
        registerDTO.setRole(role);
        registerDTO.setProfilePicture(profilePicture);
        registerDTO.setPhoneNumber(phoneNumber);
        registerDTO.setDateOfBirth(dateOfBirth);
        registerDTO.setDescription(description);
        registerDTO.setProfessionalTitle(professionalTitle);
        registerDTO.setExpertsInterests(expertsInterests);
        registerDTO.setPersonalLinks(personalLinks);

        User result = userService.registerUser(registerDTO);

        if ("Email is already registered.".equals(result)) {
            return ResponseEntity.badRequest().body(result);
        }

        User savedUser = userService.getUserByEmail(registerDTO.getEmail());

        if (savedUser == null) {
            return ResponseEntity.internalServerError().body("Registration failed.");
        }

        ProfileResponse response = new ProfileResponse(savedUser);
        return ResponseEntity.ok(response);
    
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@Validated @RequestBody LoginDTO loginDTO) {
    	 String token = jwtUtil.generateToken(loginDTO.getEmail()); 
         System.out.print(token);
    	Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword()));
        
       
        return ResponseEntity.ok(Map.of("token", token)); // إعادة التوكن في شكل JSON
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
    