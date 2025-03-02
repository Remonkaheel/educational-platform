package com.global.hr.models.DTO;

import java.time.LocalDate;

import com.global.hr.models.Role;
import com.global.hr.models.User;

public class ProfileResponse {
    private Long userId;
    private String fullName;
    private String email;
    private Role role;
    private String phoneNumber;
    private String profilePictureUrl;
    private LocalDate dateOfBirth;
    private String description;
    private static final String BASE_IMAGE_URL = "C:\\Users\\Pro Remo\\Documents\\workspace\\educational-platform-backend-6\\uploads";
    // Constructor
    public ProfileResponse(User user) {
        this.userId = user.getId();
        this.fullName = user.getFullName();
        this.email = user.getEmail();
        this.role = user.getRole();
        this.phoneNumber = user.getPhoneNumber();
        this.profilePictureUrl = user.getProfilePicture();
        this.dateOfBirth = user.getDateOfBirth();
        this.description = user.getDescription();
        
        if (user.getProfilePicture() != null && !user.getProfilePicture().isEmpty()) {
            this.profilePictureUrl = BASE_IMAGE_URL + user.getProfilePicture();
        } else {
            this.profilePictureUrl = null; // لو مفيش صورة، يرجّع `null` أو صورة افتراضية
        }
    
    }

    // Getters
    public Long getUserId() { return userId; }
    public String getFullName() { return fullName; }
    public String getEmail() { return email; }
    public Role getRole() { return role; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getProfilePictureUrl() { return profilePictureUrl; }
    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public String getDescription() { return description; }
}
