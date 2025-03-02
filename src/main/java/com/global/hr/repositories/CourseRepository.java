package com.global.hr.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.global.hr.models.Courses;

public interface CourseRepository extends JpaRepository<Courses, Long> {
}
