package com.yenyasoft.online_course_management_system.services;

import com.yenyasoft.online_course_management_system.dtos.AssignmentRequest;
import com.yenyasoft.online_course_management_system.dtos.AssignmentResponse;
import com.yenyasoft.online_course_management_system.exceptions.ResourceNotFoundException;
import com.yenyasoft.online_course_management_system.models.Assignment;
import com.yenyasoft.online_course_management_system.models.Course;
import com.yenyasoft.online_course_management_system.models.User;
import com.yenyasoft.online_course_management_system.repositories.AssignmentRepository;
import com.yenyasoft.online_course_management_system.repositories.CourseRepository;
import com.yenyasoft.online_course_management_system.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AssignmentService {

    private final AssignmentRepository assignmentRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    public AssignmentService(AssignmentRepository assignmentRepository, CourseRepository courseRepository,
                             UserRepository userRepository) {
        this.assignmentRepository = assignmentRepository;
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
    }

    public List<AssignmentResponse> getAllAssignments() {
        return assignmentRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public AssignmentResponse getAssignmentById(Long id) {
        Assignment assignment = assignmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Assignment not found with id: " + id));
        return toResponse(assignment);
    }

    public List<AssignmentResponse> getAssignmentsByCourseId(Long courseId) {
        return assignmentRepository.findByCourseCourseId(courseId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<AssignmentResponse> getAssignmentsByUserId(Long userId) {
        return assignmentRepository.findByCreatedByUserId(userId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public AssignmentResponse createAssignment(AssignmentRequest request) {
        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + request.getCourseId()));
        User user = userRepository.findById(request.getCreatedByUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + request.getCreatedByUserId()));

        Assignment assignment = Assignment.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .dueDate(request.getDueDate())
                .maxScore(request.getMaxScore())
                .course(course)
                .createdBy(user)
                .build();

        assignmentRepository.save(assignment);
        return toResponse(assignment);
    }

    @Transactional
    public AssignmentResponse updateAssignment(Long id, AssignmentRequest request) {
        Assignment assignment = assignmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Assignment not found with id: " + id));

        if (request.getTitle() != null) assignment.setTitle(request.getTitle());
        if (request.getDescription() != null) assignment.setDescription(request.getDescription());
        if (request.getDueDate() != null) assignment.setDueDate(request.getDueDate());
        if (request.getMaxScore() != null) assignment.setMaxScore(request.getMaxScore());

        if (request.getCourseId() != null) {
            Course course = courseRepository.findById(request.getCourseId())
                    .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + request.getCourseId()));
            assignment.setCourse(course);
        }
        if (request.getCreatedByUserId() != null) {
            User user = userRepository.findById(request.getCreatedByUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + request.getCreatedByUserId()));
            assignment.setCreatedBy(user);
        }

        assignmentRepository.save(assignment);
        return toResponse(assignment);
    }

    @Transactional
    public void deleteAssignment(Long id) {
        Assignment assignment = assignmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Assignment not found with id: " + id));
        assignmentRepository.delete(assignment);
    }

    private AssignmentResponse toResponse(Assignment assignment) {
        return AssignmentResponse.builder()
                .assignmentId(assignment.getAssignmentId())
                .title(assignment.getTitle())
                .description(assignment.getDescription())
                .dueDate(assignment.getDueDate())
                .maxScore(assignment.getMaxScore())
                .createdAt(assignment.getCreatedAt())
                .updatedAt(assignment.getUpdatedAt())
                .courseId(assignment.getCourse().getCourseId())
                .courseTitle(assignment.getCourse().getTitle())
                .createdByUserId(assignment.getCreatedBy().getUserId())
                .createdByUsername(assignment.getCreatedBy().getUsername())
                .build();
    }
}
