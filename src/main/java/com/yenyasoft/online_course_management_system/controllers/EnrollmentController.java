package com.yenyasoft.online_course_management_system.controllers;

import com.yenyasoft.online_course_management_system.dtos.ApiResponse;
import com.yenyasoft.online_course_management_system.dtos.EnrollmentRequest;
import com.yenyasoft.online_course_management_system.dtos.EnrollmentResponse;
import com.yenyasoft.online_course_management_system.services.EnrollmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/enrollments")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    public EnrollmentController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('INSTRUCTOR')")
    public ResponseEntity<ApiResponse<List<EnrollmentResponse>>> getAllEnrollments() {
        return ResponseEntity.ok(ApiResponse.ok("Enrollments fetched successfully", enrollmentService.getAllEnrollments()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<EnrollmentResponse>> getEnrollmentById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok("Enrollment fetched successfully", enrollmentService.getEnrollmentById(id)));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<EnrollmentResponse>>> getEnrollmentsByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(ApiResponse.ok("Enrollments fetched successfully", enrollmentService.getEnrollmentsByUserId(userId)));
    }

    @GetMapping("/course/{courseId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INSTRUCTOR')")
    public ResponseEntity<ApiResponse<List<EnrollmentResponse>>> getEnrollmentsByCourseId(@PathVariable Long courseId) {
        return ResponseEntity.ok(ApiResponse.ok("Enrollments fetched successfully", enrollmentService.getEnrollmentsByCourseId(courseId)));
    }

    @PostMapping
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<ApiResponse<EnrollmentResponse>> createEnrollment(@RequestBody EnrollmentRequest request) {
        return new ResponseEntity<>(ApiResponse.ok("Enrollment created successfully", enrollmentService.createEnrollment(request)), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INSTRUCTOR')")
    public ResponseEntity<ApiResponse<EnrollmentResponse>> updateEnrollment(@PathVariable Long id, @RequestBody EnrollmentRequest request) {
        return ResponseEntity.ok(ApiResponse.ok("Enrollment updated successfully", enrollmentService.updateEnrollment(id, request)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteEnrollment(@PathVariable Long id) {
        enrollmentService.deleteEnrollment(id);
        return new ResponseEntity<>(ApiResponse.ok("Enrollment deleted successfully"), HttpStatus.NO_CONTENT);
    }
}
