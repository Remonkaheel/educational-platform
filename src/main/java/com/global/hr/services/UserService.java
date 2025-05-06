package com.global.hr.services;

import com.global.hr.models.*;
import com.global.hr.models.DTO.LoginDTO;
import com.global.hr.models.DTO.RegisterDTO;
import com.global.hr.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
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
    @Autowired
    private CategoryRepository categoryRepository;
   
    private PasswordEncoder passwordEncoder;

    
    public User registerUser(RegisterDTO registerDTO) throws IOException {
    	
    	 System.out.println("Received in Service: " + registerDTO.getExpertsInterests());
        Optional<User> existingUser = userRepository.findByEmail(registerDTO.getEmail());
        if (existingUser.isPresent()) {
        	return null;
        }
       
       String profilePictureUrl = "default-profile-url";
        try {
           System.out.println("Received file: " + registerDTO.getProfilePicture().getOriginalFilename());
             profilePictureUrl = fileStorageService.storeFile(registerDTO.getProfilePicture());
            System.out.println("File saved at: " + profilePictureUrl);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to upload file!", e);
        }
        User user;
        
        if (registerDTO.getRole().equalsIgnoreCase("INSTRUCTOR")) {
            Instructor instructor = new Instructor();
            instructor.setRole("INSTRUCTOR");
            instructor.setProfessionalTitle(registerDTO.getProfessionalTitle());
            instructor.setPersonalLinks(registerDTO.getPersonalLinks());
            user = instructorRepository.save(instructor);
        } else {
            Student student = new Student();
            student.setRole("STUDENT");
            user = studentRepository.save(student);
        }

        user.setFullName(registerDTO.getFullName());
        user.setEmail(registerDTO.getEmail());
        user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        user.setPhoneNumber(registerDTO.getPhoneNumber());
        user.setDateOfBirth(registerDTO.getDateOfBirth());
        user.setDescription(registerDTO.getDescription());
        
        if (registerDTO.getProfilePicture() != null && !registerDTO.getProfilePicture().isEmpty()) {
            String profileUrl = fileStorageService.storeFile(registerDTO.getProfilePicture());
            user.setProfilePicture(profileUrl);
        }
        System.out.println("Looking for categories: " + registerDTO.getExpertsInterests());

        List<Category> categories = categoryRepository.findByNameIn(registerDTO.getExpertsInterests());
        System.out.println("Fetched Categories: " + categories); // ✅ تأكد من النتيجة

        if (categories != null && !categories.isEmpty()) {
            user.setExpertsInterests(categories);
        } else {
            System.out.println("⚠️ Warning: No categories found for the given names!");
        }
        userRepository.save(user);

        return user;
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

}