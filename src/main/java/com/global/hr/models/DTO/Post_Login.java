package com.global.hr.models.DTO;



public class Post_Login {

    private Long id;
    private String role; // Use Role enum directly

    // Constructor
    public Post_Login(Long id, String role) {
        this.id = id;
        this.role = role;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
