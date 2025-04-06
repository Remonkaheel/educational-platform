package com.global.hr.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.global.hr.models.Student;
import com.global.hr.repositories.StudentRepository;

@Service
public class StudentService {

	
	    @Autowired
	    private StudentRepository studentRepository;

	    public Student getStudentById(Long id) {
	        return studentRepository.findById(id)
	            .orElseThrow(() -> new RuntimeException("Student not found!"));
	    }

	    public void updateStudent(Student student) {
	        studentRepository.save(student);
	    }
	

}