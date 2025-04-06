package com.global.hr.models;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name", unique = true, nullable = false)
    private String name;
    private Long numberOfCourses = 0L;
    private String catPicture;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Course> courses;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCatName() { return name; }
    public void setCatName(String catName) { this.name = catName; }

    public Long getNumberOfCourses() { return numberOfCourses; }
    public void setNumberOfCourses(Long numberOfCourses) { this.numberOfCourses = numberOfCourses; }

    public String getCatPicture() { return catPicture; }
    public void setCatPicture(String catPicture) { this.catPicture = catPicture; }

    public List<Course> getCourses() { return courses; }
    public void setCourses(List<Course> courses) { this.courses = courses; }
}
