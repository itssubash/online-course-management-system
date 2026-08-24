package com.yenyasoft.online_course_management_system.services;

import com.yenyasoft.online_course_management_system.dtos.SubmissionRequest;
import com.yenyasoft.online_course_management_system.dtos.SubmissionResponse;
import com.yenyasoft.online_course_management_system.exceptions.ResourceNotFoundException;
import com.yenyasoft.online_course_management_system.models.Assignment;
import com.yenyasoft.online_course_management_system.models.Submission;
import com.yenyasoft.online_course_management_system.models.User;
import com.yenyasoft.online_course_management_system.repositories.AssignmentRepository;
import com.yenyasoft.online_course_management_system.repositories.SubmissionRepository;
import com.yenyasoft.online_course_management_system.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubmissionService {

    private final SubmissionRepository submissionRepository;
    private final AssignmentRepository assignmentRepository;
    private final UserRepository userRepository;

    public SubmissionService(SubmissionRepository submissionRepository, AssignmentRepository assignmentRepository,
                             UserRepository userRepository) {
        this.submissionRepository = submissionRepository;
        this.assignmentRepository = assignmentRepository;
        this.userRepository = userRepository;
    }

    public List<SubmissionResponse> getAllSubmissions() {
        return submissionRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public SubmissionResponse getSubmissionById(Long id) {
        Submission submission = submissionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Submission not found with id: " + id));
        return toResponse(submission);
    }

    public List<SubmissionResponse> getSubmissionsByAssignmentId(Long assignmentId) {
        return submissionRepository.findByAssignmentAssignmentId(assignmentId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<SubmissionResponse> getSubmissionsByStudentId(Long studentId) {
        return submissionRepository.findByStudentUserId(studentId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public SubmissionResponse createSubmission(SubmissionRequest request) {
        Assignment assignment = assignmentRepository.findById(request.getAssignmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Assignment not found with id: " + request.getAssignmentId()));
        User student = userRepository.findById(request.getStudentUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + request.getStudentUserId()));

        Submission submission = Submission.builder()
                .content(request.getContent())
                .fileUrl(request.getFileUrl())
                .score(request.getScore())
                .feedback(request.getFeedback())
                .submissionStatus(request.getSubmissionStatus())
                .assignment(assignment)
                .student(student)
                .build();

        submissionRepository.save(submission);
        return toResponse(submission);
    }

    @Transactional
    public SubmissionResponse updateSubmission(Long id, SubmissionRequest request) {
        Submission submission = submissionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Submission not found with id: " + id));

        if (request.getContent() != null) submission.setContent(request.getContent());
        if (request.getFileUrl() != null) submission.setFileUrl(request.getFileUrl());
        if (request.getScore() != null) submission.setScore(request.getScore());
        if (request.getFeedback() != null) submission.setFeedback(request.getFeedback());
        if (request.getSubmissionStatus() != null) submission.setSubmissionStatus(request.getSubmissionStatus());

        if (request.getAssignmentId() != null) {
            Assignment assignment = assignmentRepository.findById(request.getAssignmentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Assignment not found with id: " + request.getAssignmentId()));
            submission.setAssignment(assignment);
        }
        if (request.getStudentUserId() != null) {
            User student = userRepository.findById(request.getStudentUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + request.getStudentUserId()));
            submission.setStudent(student);
        }

        submissionRepository.save(submission);
        return toResponse(submission);
    }

    @Transactional
    public void deleteSubmission(Long id) {
        Submission submission = submissionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Submission not found with id: " + id));
        submissionRepository.delete(submission);
    }

    private SubmissionResponse toResponse(Submission submission) {
        return SubmissionResponse.builder()
                .submissionId(submission.getSubmissionId())
                .content(submission.getContent())
                .fileUrl(submission.getFileUrl())
                .submittedAt(submission.getSubmittedAt())
                .score(submission.getScore())
                .feedback(submission.getFeedback())
                .submissionStatus(submission.getSubmissionStatus())
                .assignmentId(submission.getAssignment().getAssignmentId())
                .assignmentTitle(submission.getAssignment().getTitle())
                .studentUserId(submission.getStudent().getUserId())
                .studentUsername(submission.getStudent().getUsername())
                .build();
    }
}
