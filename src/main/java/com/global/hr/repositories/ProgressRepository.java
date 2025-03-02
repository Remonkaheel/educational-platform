package com.global.hr.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.global.hr.models.CourseProgress;

public interface ProgressRepository extends JpaRepository<CourseProgress, Long> {
    List<CourseProgress> findByUserIdAndDateBetween(Long userId, LocalDate start, LocalDate end);
}