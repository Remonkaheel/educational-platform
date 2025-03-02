package com.global.hr.controllers;

import com.global.hr.models.DTO.*;
import com.global.hr.services.StudentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/dashboard/{studentId}")
    public ResponseEntity<DashboardResponse> getStudentDashboard(@PathVariable Long studentId) {
        return ResponseEntity.ok(studentService.getDashboardData(studentId));
    }
}