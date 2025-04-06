package com.global.hr.models.DTO;

import java.time.LocalDate;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public class RegisterDTO {
    private String fullName;
    private String email;
    private String password;
    private String role;
    private MultipartFile profilePicture;
    private String phoneNumber;
    private LocalDate dateOfBirth;
    private String description;
    private String professionalTitle;
    private List<String> expertsInterests;
    private List<String> personalLinks;

    public RegisterDTO(String fullName, String email, String password, String role, MultipartFile profilePicture,
                       String phoneNumber, LocalDate dateOfBirth, String description, String professionalTitle,
                       List<String> expertsInterests, List<String> personalLinks) {
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.role = role;
        this.profilePicture = profilePicture;
        this.phoneNumber = phoneNumber;
        this.dateOfBirth = dateOfBirth;
        this.description = description;
        this.professionalTitle = professionalTitle;
        this.expertsInterests = expertsInterests;
        this.personalLinks = personalLinks;
    }

    // Getters & Setters



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


	

	public MultipartFile getProfilePicture() {
		return profilePicture;
	}

	public void setProfilePicture(MultipartFile profilePicture) {
		this.profilePicture = profilePicture;
	}

	public List<String> getExpertsInterests() {
		return expertsInterests;
	}

	public void setExpertsInterests(List<String> expertsInterests) {
		this.expertsInterests = expertsInterests;
	}
}
