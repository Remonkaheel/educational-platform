package com.global.hr.controllers;

import com.global.hr.models.Category;
import com.global.hr.models.Course;
import com.global.hr.models.Instructor;
import com.global.hr.models.DTO.CourseRequest;
import com.global.hr.repositories.CategoryRepository;
import com.global.hr.repositories.CourseRepository;
import com.global.hr.repositories.InstructorRepository;
import com.global.hr.services.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/courses")
public class CourseController {
    @Autowired
    private CourseService courseService;
    
    @Autowired
    private InstructorRepository instructorRepository;
    @Autowired
    private CategoryRepository  categoryRepository;
    @Autowired
    private CourseRepository courseRepository;
    @GetMapping
    public List<Course> getAllCourses() {
        return courseService.getAllCourses();
    }

    @GetMapping("/{id}")
    public Optional<Course> getCourseById(@PathVariable Long id) {
        return courseService.getCourseById(id);
    }

    @GetMapping("/category/{categoryId}")
    public List<Course> getCoursesByCategory(@PathVariable Long categoryId) {
        return courseService.getCoursesByCategory(categoryId);
    }

    @GetMapping("/instructor/{instructorId}")
    public List<Course> getCoursesByInstructor(@PathVariable Long instructorId) {
        return courseService.getCoursesByInstructor(instructorId);
    }

    @PostMapping("/add")
    public ResponseEntity<?> createCourse(@RequestBody CourseRequest courseRequest) {
        // جلب الإنستراكتور من قاعدة البيانات
        Instructor instructor = instructorRepository.findById(courseRequest.getInstructorId())
            .orElseThrow(() -> new RuntimeException("Instructor not found!"));

        // جلب الكاتيجوري من قاعدة البيانات
        Category category = categoryRepository.findById(courseRequest.getCategoryId())
            .orElseThrow(() -> new RuntimeException("Category not found!"));

        // إنشاء الكورس
        Course course = new Course();
        course.setCourseName(courseRequest.getCourseName());
        course.setDescription(courseRequest.getDescription());
        course.setAuthor(instructor);  // لازم نعيّن الـ Instructor هنا
        course.setCategory(category);

        // حفظ الكورس
        courseRepository.save(course);

        return ResponseEntity.ok("Course created successfully!");
    }


//    public Course addCourse(@RequestBody Course course) {
//        return courseService.addCourse(course);
//    }

    @DeleteMapping("/{id}")
    public void deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
    }
}
