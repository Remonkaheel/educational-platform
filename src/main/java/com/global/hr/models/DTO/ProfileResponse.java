package com.global.hr.models.DTO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.global.hr.models.Category;
import com.global.hr.models.Course;
import com.global.hr.models.Instructor;
import com.global.hr.models.Role;
import com.global.hr.models.Student;
import com.global.hr.models.User;
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProfileResponse {
    private static final String BASE_IMAGE_URL = "images/";
    private Long id;
    private String fullName;
    private String email;
    private String phoneNumber;
    private String profilePictureUrl;
    private Role role;
    private String description;
    private LocalDate dateOfBirth;
    private List<Map<String,Object>> expertsInterests;
    private List<Course> courses;
    private String token;
    // Instructor-specific fields
    private String professionalTitle;
    private List<String> personalLinks;
    private Integer totalStudents;
    private Map<Double, LocalDateTime> revenuePoints;

    // Student-specific fields
    private Map<LocalDateTime,Integer> activityPoints;
	

    // Constructor
    public ProfileResponse(User user,String token) {
        this.id = user.getId();
        this.fullName = user.getFullName();
        this.email = user.getEmail();
        this.role = user.getRole();
        this.phoneNumber = user.getPhoneNumber();
        this.profilePictureUrl = user.getProfilePicture();
        this.dateOfBirth = user.getDateOfBirth();
        this.description = user.getDescription();
        this.token = token;
        if (user.getExpertsInterests() != null) {
        	this.expertsInterests = user.getExpertsInterests().stream()
        		    .map(category -> {
        		        Map<String, Object> simpleMap = new HashMap<>();
        		        simpleMap.put("id", category.getId());
        		        simpleMap.put("catName", category.getCatName());
        		        return simpleMap;
        		    })
        		    .collect(Collectors.toList());
        } else {
            this.expertsInterests = Collections.emptyList();
        }
        if (user.getProfilePicture() != null && !user.getProfilePicture().isEmpty()) {
            this.profilePictureUrl = BASE_IMAGE_URL + user.getProfilePicture();
        } else {
            this.profilePictureUrl = null; // لو مفيش صورة، يرجّع `null` أو صورة افتراضية
        }
    
    if (user.getRole() == Role.INSTRUCTOR) {
        Instructor instructor = (Instructor) user;
        this.professionalTitle = instructor.getProfessionalTitle();
        this.personalLinks = instructor.getPersonalLinks();
        this.totalStudents = Integer.valueOf(instructor.getTotalStudents());
        this.revenuePoints = instructor.getRevenuePoints();
        this.courses = instructor.getCourses();
    } else if (user.getRole() == Role.STUDENT) {
        Student student = (Student) user;
        this.activityPoints = student.getActivityPoints();
        this.courses = student.getCourses();
    }// الكورسات اللي الطالب مسجل فيها
    }

    // Getters
    //public Long getUserId() { return id; }
    public String getFullName() { return fullName; }
    public String getEmail() { return email; }
    public Role getRole() { return role; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getProfilePictureUrl() { return profilePictureUrl; }
    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public String getDescription() { return description; }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public List<Course> getCourses() {
		return courses;
	}

	public void setCourses(List<Course> courses) {
		this.courses = courses;
	}

	public Integer getTotalStudents() {
		return totalStudents;
	}

	public void setTotalStudents(Integer totalStudents) {
		this.totalStudents = totalStudents;
	}

	public List<String> getPersonalLinks() {
		return personalLinks;
	}

	public void setPersonalLinks(List<String> personalLinks) {
		this.personalLinks = personalLinks;
	}

	public String getProfessionalTitle() {
		return professionalTitle;
	}

	public void setProfessionalTitle(String professionalTitle) {
		this.professionalTitle = professionalTitle;
	}

	public Map<Double, LocalDateTime> getRevenuePoints() {
		return revenuePoints;
	}

	public void setRevenuePoints(Map<Double, LocalDateTime> revenuePoints) {
		this.revenuePoints = revenuePoints;
	}

	public Map<LocalDateTime,Integer> getActivityPoints() {
		return activityPoints;
	}

	public void setActivityPoints(Map<LocalDateTime,Integer> activityPoints) {
		this.activityPoints = activityPoints;
	}

	public static String getBaseImageUrl() {
		return BASE_IMAGE_URL;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public void setProfilePictureUrl(String profilePictureUrl) {
		this.profilePictureUrl = profilePictureUrl;
	}

	public void setDateOfBirth(LocalDate dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public void setDescription(String description) {
		this.description = description;
	}


	public List<Map<String, Object>> getExpertsInterests() {
		return expertsInterests;
	}


	public void setExpertsInterests(List<Map<String, Object>> expertsInterests) {
		this.expertsInterests = expertsInterests;
	}

	
}
