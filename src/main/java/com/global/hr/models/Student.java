package com.global.hr.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "student")
@PrimaryKeyJoinColumn(name = "user_id", referencedColumnName = "id")
@DiscriminatorValue("STUDENT")
public class Student extends User {

    @ElementCollection
    @CollectionTable(name = "student_activity_points",
                     joinColumns = @JoinColumn(name = "student_id"))
    @MapKeyColumn(name = "timestamp")
    @Column(name = "points")
    private Map<LocalDateTime, Integer> activityPoints;

    @ManyToMany
    @JoinTable(
        name = "student_courses",
        joinColumns = @JoinColumn(name = "student_id"),
        inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    private List<Course> courses;


    // Getters & Setters
	
	public List<Course> getCourses() {
		return courses;
	}
	public void setCourses(List<Course> courses) {
		this.courses = courses;
	}
	public Map<LocalDateTime, Integer> getActivityPoints() {
		return activityPoints;
	}
	public void setActivityPoints(Map<LocalDateTime, Integer> activityPoints) {
		this.activityPoints = activityPoints;
	}
	
	

}