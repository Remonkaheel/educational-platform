package com.global.hr.models.DTO;

public class CourseRequest {
	
	    private String courseName;
	    private String description;
	    private Long instructorId;
	    private Long categoryId;
	    
	    
	 // Getters and Setters
		public String getCourseName() {
			return courseName;
		}
		public void setCourseName(String courseName) {
			this.courseName = courseName;
		}
		public String getDescription() {
			return description;
		}
		public void setDescription(String description) {
			this.description = description;
		}
		public Long getInstructorId() {
			return instructorId;
		}
		public void setInstructorId(Long instructorId) {
			this.instructorId = instructorId;
		}
		public Long getCategoryId() {
			return categoryId;
		}
		public void setCategoryId(Long categoryId) {
			this.categoryId = categoryId;
		}

	    
	}


