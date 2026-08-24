package com.yenyasoft.online_course_management_system.controllers;

import com.yenyasoft.online_course_management_system.dtos.ApiResponse;
import com.yenyasoft.online_course_management_system.dtos.SubmissionRequest;
import com.yenyasoft.online_course_management_system.dtos.SubmissionResponse;
import com.yenyasoft.online_course_management_system.services.SubmissionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/submissions")
public class SubmissionController {

    private final SubmissionService submissionService;

    public SubmissionController(SubmissionService submissionService) {
        this.submissionService = submissionService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('INSTRUCTOR')")
    public ResponseEntity<ApiResponse<List<SubmissionResponse>>> getAllSubmissions() {
        return ResponseEntity.ok(ApiResponse.ok("Submissions fetched", submissionService.getAllSubmissions()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SubmissionResponse>> getSubmissionById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok("Submission fetched", submissionService.getSubmissionById(id)));
    }

    @GetMapping("/assignment/{assignmentId}")
    @PreAuthorize("hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<SubmissionResponse>>> getByAssignment(@PathVariable Long assignmentId) {
        return ResponseEntity.ok(ApiResponse.ok("Submissions fetched", submissionService.getSubmissionsByAssignmentId(assignmentId)));
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<ApiResponse<List<SubmissionResponse>>> getByStudent(@PathVariable Long studentId) {
        return ResponseEntity.ok(ApiResponse.ok("Submissions fetched", submissionService.getSubmissionsByStudentId(studentId)));
    }

    @PostMapping
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<ApiResponse<SubmissionResponse>> createSubmission(@RequestBody SubmissionRequest request) {
        return new ResponseEntity<>(ApiResponse.ok("Submission created", submissionService.createSubmission(request)), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<SubmissionResponse>> updateSubmission(@PathVariable Long id, @RequestBody SubmissionRequest request) {
        return ResponseEntity.ok(ApiResponse.ok("Submission updated", submissionService.updateSubmission(id, request)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteSubmission(@PathVariable Long id) {
        submissionService.deleteSubmission(id);
        return new ResponseEntity<>(ApiResponse.ok("Submission deleted"), HttpStatus.NO_CONTENT);
    }
}
