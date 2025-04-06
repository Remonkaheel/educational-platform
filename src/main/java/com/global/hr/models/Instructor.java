package com.global.hr.models;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import jakarta.persistence.*;

@Entity
@Table(name = "instructor")
@DiscriminatorValue("INSTRUCTOR")
@PrimaryKeyJoinColumn(name = "user_id", referencedColumnName = "id")
public class Instructor extends User {

    private String professionalTitle;

    @ElementCollection
    @CollectionTable(name = "instructor_personal_links", 
                     joinColumns = @JoinColumn(name = "instructor_id"))
    @Column(name = "link")
    private List<String> personalLinks;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Course> courses;

    private int totalStudents;
    
    @ElementCollection
    @CollectionTable(name = "instructor_revenue_points",
                     joinColumns = @JoinColumn(name = "instructor_id"))
    @MapKeyColumn(name = "amount")
    @Column(name = "timestamp")
    private Map<Double, LocalDateTime> revenuePoints;

    // Getters & Setters

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

	public List<Course> getCourses() {
		return courses;
	}

	public void setCourses(List<Course> courses) {
		this.courses = courses;
	}

	public int getTotalStudents() {
		return totalStudents;
	}

	public void setTotalStudents(int totalStudents) {
		this.totalStudents = totalStudents;
	}

	

	public Map<Double, LocalDateTime> getRevenuePoints() {
		return revenuePoints;
	}

	public void setRevenuePoints(Map<Double, LocalDateTime> revenuePoints) {
		this.revenuePoints = revenuePoints;
	}

	
}