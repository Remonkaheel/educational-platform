package com.global.hr.models;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
@Entity
@Table(name = "app_users") // تجنب استخدام "user" (كلمة محجوزة في MySQL)
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; 

    private String fullName;
    private String email;
    private String password;
    
    @Enumerated(EnumType.STRING)
    private Role role;
    private String phoneNumber;  // اختياري
    private String profilePicture;  // اختياري، رابط للصورة
    private LocalDate dateOfBirth;
    @Column(length = 500)
    private String description;
    @ElementCollection
    @CollectionTable(name = "experts_interests", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "value")
    private List<String> expertsInterests = new ArrayList<>(); 
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<CourseProgress> courseProgresses = new ArrayList<>();
    // Getters and Setters (لتعامل الكود مع البيانات)
    private String otp;
    private LocalDateTime otpExpiry;
    
    public String getOtp() { return otp; }
    public void setOtp(String otp) { this.otp = otp; }

    public LocalDateTime getOtpExpiry() { return otpExpiry; }
    public void setOtpExpiry(LocalDateTime otpExpiry) { this.otpExpiry = otpExpiry; }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
    public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	public List<String> getExpertsInterests() {
        return expertsInterests;
    }

    public void setExpertsInterests(List<String> expertsInterests) {
        this.expertsInterests = expertsInterests;
    }

	public List<CourseProgress> getCourseProgresses() {
		return courseProgresses;
	}

	public void setCourseProgresses(List<CourseProgress> courseProgresses) {
		this.courseProgresses = courseProgresses;
	}

    }
