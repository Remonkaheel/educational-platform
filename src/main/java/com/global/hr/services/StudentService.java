package com.global.hr.services;

import com.global.hr.models.CourseProgress;
import com.global.hr.models.Student;
import com.global.hr.models.DTO.DashboardResponse;
import com.global.hr.repositories.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public DashboardResponse getDashboardData(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not in STUDENT table"));

        return new DashboardResponse(
                student.getFullName(),
                student.getCourseProgresses().size(),
                calculateTotalHours(student),
                calculateRecentHours(student),
                calculateLast3DaysProgress(student)
        );
    }

    private int calculateTotalHours(Student student) {
        return student.getCourseProgresses().stream()
                .mapToInt(CourseProgress::getHoursSpent)
                .sum();
    }

    private int calculateRecentHours(Student student) {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(2);
        
        return student.getCourseProgresses().stream()
                .filter(cp -> !cp.getDate().isBefore(startDate))
                .mapToInt(CourseProgress::getHoursSpent)
                .sum();
    }
    
    private Map<LocalDate, Integer> calculateLast3DaysProgress(Student student) {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(2); // آخر 3 أيام (بما في اليوم الحالي)
        
        return student.getCourseProgresses().stream()
            .filter(cp -> !cp.getDate().isBefore(startDate)) // تصفية السجلات في آخر 3 أيام
            .collect(Collectors.groupingBy(
                CourseProgress::getDate, // التجميع حسب التاريخ
                Collectors.summingInt(CourseProgress::getHoursSpent) // جمع الساعات لكل تاريخ
            ));
    }
}