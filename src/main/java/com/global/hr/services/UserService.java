package com.global.hr.services;

import com.global.hr.models.*;
import com.global.hr.models.DTO.LoginDTO;
import com.global.hr.models.DTO.RegisterDTO;
import com.global.hr.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private InstructorRepository instructorRepository;

    @Autowired
    private FileStorageService fileStorageService;
   
    private PasswordEncoder passwordEncoder;

    
    public User registerUser(RegisterDTO registerDTO) {
        Optional<User> existingUser = userRepository.findByEmail(registerDTO.getEmail());
        if (existingUser.isPresent()) {
        	return null;
        }

       String profilePictureUrl = "default-profile-url";
        try {
           // System.out.println("Received file: " + registerDTO.getProfilePicture().getOriginalFilename());
             profilePictureUrl = fileStorageService.storeFile(registerDTO.getProfilePicture());
            System.out.println("File saved at: " + profilePictureUrl);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to upload file!", e);
        }

        if ("STUDENT".equals(registerDTO.getRole())) {
            Student student = new Student();
            mapCommonFields(student, registerDTO);
            student.setProfilePicture(profilePictureUrl);
            student.setExpertsInterests(registerDTO.getExpertsInterests());
            studentRepository.save(student);
        } else if ("INSTRUCTOR".equals(registerDTO.getRole())) {
            Instructor instructor = new Instructor();
            mapCommonFields(instructor, registerDTO);
            instructor.setProfilePicture(profilePictureUrl);
            instructor.setExpertsInterests(registerDTO.getExpertsInterests());
            instructorRepository.save(instructor);
        	
        }
        return null;
    }
    public User loginUser(LoginDTO loginDTO) {
        User user = userRepository.findByEmail(loginDTO.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            return user;
        } else {
            throw new RuntimeException("Invalid password");
        }
    }
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }
    public UserService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }
    public String encodePassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    private void mapCommonFields(User user, RegisterDTO registerDTO) {
        user.setFullName(registerDTO.getFullName());
        user.setEmail(registerDTO.getEmail());
        String encodedPassword = passwordEncoder.encode(registerDTO.getPassword());
        user.setPassword(encodedPassword);
        user.setPhoneNumber(registerDTO.getPhoneNumber());
        user.setDateOfBirth(registerDTO.getDateOfBirth());
        user.setDescription(registerDTO.getDescription());
        user.setRole(Role.valueOf(registerDTO.getRole()));
    }
}