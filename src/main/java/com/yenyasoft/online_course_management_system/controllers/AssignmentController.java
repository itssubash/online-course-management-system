package com.yenyasoft.online_course_management_system.controllers;

import com.yenyasoft.online_course_management_system.dtos.ApiResponse;
import com.yenyasoft.online_course_management_system.dtos.AssignmentRequest;
import com.yenyasoft.online_course_management_system.dtos.AssignmentResponse;
import com.yenyasoft.online_course_management_system.services.AssignmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/assignments")
public class AssignmentController {

    private final AssignmentService assignmentService;

    public AssignmentController(AssignmentService assignmentService) {
        this.assignmentService = assignmentService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<AssignmentResponse>>> getAllAssignments() {
        return ResponseEntity.ok(ApiResponse.ok("Assignments fetched successfully", assignmentService.getAllAssignments()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AssignmentResponse>> getAssignmentById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok("Assignment fetched successfully", assignmentService.getAssignmentById(id)));
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<ApiResponse<List<AssignmentResponse>>> getAssignmentsByCourseId(@PathVariable Long courseId) {
        return ResponseEntity.ok(ApiResponse.ok("Assignments fetched successfully", assignmentService.getAssignmentsByCourseId(courseId)));
    }

    @PostMapping
    @PreAuthorize("hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<AssignmentResponse>> createAssignment(@RequestBody AssignmentRequest request) {
        return new ResponseEntity<>(ApiResponse.ok("Assignment created successfully", assignmentService.createAssignment(request)), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<AssignmentResponse>> updateAssignment(@PathVariable Long id, @RequestBody AssignmentRequest request) {
        return ResponseEntity.ok(ApiResponse.ok("Assignment updated successfully", assignmentService.updateAssignment(id, request)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteAssignment(@PathVariable Long id) {
        assignmentService.deleteAssignment(id);
        return new ResponseEntity<>(ApiResponse.ok("Assignment deleted successfully"), HttpStatus.NO_CONTENT);
    }
}
