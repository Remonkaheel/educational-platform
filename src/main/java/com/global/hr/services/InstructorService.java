package com.global.hr.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.global.hr.models.Instructor;
import com.global.hr.repositories.InstructorRepository;

@Service
public class InstructorService {
    @Autowired
    private InstructorRepository instructorRepository;

    public Instructor getInstructorById(Long id) {
        return instructorRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Instructor not found!"));
    }

    public void updateInstructor(Instructor instructor) {
        instructorRepository.save(instructor);
    }
}
