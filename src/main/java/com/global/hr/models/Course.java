package com.global.hr.models;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "courses")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "instructor_id", nullable = false)
    private Instructor author;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    private String courseName;
    private String description;
    private double rating = 0.0;
    private long numberOfEnrolled = 0;
    private String coverPicture;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Video> videos;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Instructor getAuthor() { return author; }
    public void setAuthor(Instructor author) { this.author = author; }

    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }

    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getRating() { return rating; }
    public void setRating(double rating) { this.rating = rating; }

    public long getNumberOfEnrolled() { return numberOfEnrolled; }
    public void setNumberOfEnrolled(long numberOfEnrolled) { this.numberOfEnrolled = numberOfEnrolled; }

    public String getCoverPicture() { return coverPicture; }
    public void setCoverPicture(String coverPicture) { this.coverPicture = coverPicture; }

    public List<Video> getVideos() { return videos; }
    public void setVideos(List<Video> videos) { this.videos = videos; }
}
