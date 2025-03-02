package com.global.hr.models;

import java.time.LocalDate;

import jakarta.persistence.*;

@Entity
public class CourseProgress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    
    @ManyToOne
    @JoinColumn(name = "course_id")
    private Courses course;
    
    private LocalDate date;
    private int hoursSpent;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Courses getCourse() {
		return course;
	}
	public void setCourse(Courses course) {
		this.course = course;
	}
	public LocalDate getDate() {
		return date;
	}
	public void setDate(LocalDate date) {
		this.date = date;
	}
	public int getHoursSpent() {
		return hoursSpent;
	}
	public void setHoursSpent(int hoursSpent) {
		this.hoursSpent = hoursSpent;
	}
}