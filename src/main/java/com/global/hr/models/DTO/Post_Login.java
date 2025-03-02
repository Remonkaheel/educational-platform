package com.global.hr.models.DTO;

import com.global.hr.models.Role;

public class Post_Login {

    private Long id;
    private Role role; // Use Role enum directly

    // Constructor
    public Post_Login(Long id, Role role) {
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

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
