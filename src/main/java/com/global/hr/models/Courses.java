package com.global.hr.models;

import jakarta.persistence.*;

@Entity

public class Courses {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private int totalHours;
    private boolean enrolled;
    private String thumbnailUrl;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public int getTotalHours() {
		return totalHours;
	}
	public void setTotalHours(int totalHours) {
		this.totalHours = totalHours;
	}
	public boolean isEnrolled() {
		return enrolled;
	}
	public void setEnrolled(boolean enrolled) {
		this.enrolled = enrolled;
	}
	public String getThumbnailUrl() {
		return thumbnailUrl;
	}
	public void setThumbnailUrl(String thumbnailUrl) {
		this.thumbnailUrl = thumbnailUrl;
	}
}
