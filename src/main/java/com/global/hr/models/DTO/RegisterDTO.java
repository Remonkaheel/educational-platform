package com.global.hr.models.DTO;

import java.time.LocalDate;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public class RegisterDTO {

    private String fullName;
    private String email;
    private String password;
    private String role;  // STUDENT or INSTRUCTOR
    private String phoneNumber;
    private MultipartFile profilePicture; 
    private LocalDate dateOfBirth;
    private String description;

    private String professionalTitle;
    private List<String> expertsinterests;
    private List<String> personalLinks;

    // Getters and Setters
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role.toUpperCase();
    }

    public void setRole(String role) {
        this.role = role.toUpperCase();
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

   

    public String getProfessionalTitle() {
        return professionalTitle;
    }

    public void setProfessionalTitle(String professionalTitle) {
        this.professionalTitle = professionalTitle;
    }

    

    public List<String> getPersonalLinks() {
        return personalLinks;
    }

    public void setPersonalLinks(List<String> personalLinks) {
        this.personalLinks = personalLinks;
    }

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}


	public List<String> getExpertsInterests() {
		return expertsinterests;
	}

	public void setExpertsInterests(List<String> expertsinterests) {
		this.expertsinterests = expertsinterests;
	}

	public MultipartFile getProfilePicture() {
		return profilePicture;
	}

	public void setProfilePicture(MultipartFile profilePicture) {
		this.profilePicture = profilePicture;
	}
}
