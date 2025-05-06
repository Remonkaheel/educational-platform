package com.global.hr.models.DTO;

import com.global.hr.models.Course;

public class CourseDTO {
    private Long id;
    private String name;

    public CourseDTO(Course course) {
        this.id = course.getId();
        this.name = course.getCourseName(); 
        }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

    // getters and setters
}
