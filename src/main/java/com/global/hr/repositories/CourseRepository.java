package com.global.hr.repositories;

import com.global.hr.models.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findByCategoryId(Long categoryId);
    List<Course> findByAuthorId(Long instructorId);
}
