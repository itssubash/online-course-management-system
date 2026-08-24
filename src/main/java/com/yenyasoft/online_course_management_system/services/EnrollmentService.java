package com.yenyasoft.online_course_management_system.services;

import com.yenyasoft.online_course_management_system.dtos.EnrollmentRequest;
import com.yenyasoft.online_course_management_system.dtos.EnrollmentResponse;
import com.yenyasoft.online_course_management_system.enums.EnrollmentStatus;
import com.yenyasoft.online_course_management_system.exceptions.AlreadyExistsException;
import com.yenyasoft.online_course_management_system.exceptions.ResourceNotFoundException;
import com.yenyasoft.online_course_management_system.models.Course;
import com.yenyasoft.online_course_management_system.models.Enrollment;
import com.yenyasoft.online_course_management_system.models.User;
import com.yenyasoft.online_course_management_system.repositories.CourseRepository;
import com.yenyasoft.online_course_management_system.repositories.EnrollmentRepository;
import com.yenyasoft.online_course_management_system.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;

    public EnrollmentService(EnrollmentRepository enrollmentRepository, UserRepository userRepository,
                             CourseRepository courseRepository) {
        this.enrollmentRepository = enrollmentRepository;
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
    }

    public List<EnrollmentResponse> getAllEnrollments() {
        return enrollmentRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public EnrollmentResponse getEnrollmentById(Long id) {
        Enrollment enrollment = enrollmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment not found with id: " + id));
        return toResponse(enrollment);
    }

    public List<EnrollmentResponse> getEnrollmentsByUserId(Long userId) {
        return enrollmentRepository.findByUserUserId(userId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<EnrollmentResponse> getEnrollmentsByCourseId(Long courseId) {
        return enrollmentRepository.findByCourseCourseId(courseId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public EnrollmentResponse createEnrollment(EnrollmentRequest request) {
        if (enrollmentRepository.existsByUserUserIdAndCourseCourseId(request.getUserId(), request.getCourseId())) {
            throw new AlreadyExistsException("User is already enrolled in this course");
        }

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + request.getUserId()));
        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + request.getCourseId()));

        Enrollment enrollment = Enrollment.builder()
                .user(user)
                .course(course)
                .status(request.getStatus() != null ? request.getStatus() : EnrollmentStatus.ENROLLED)
                .build();

        enrollmentRepository.save(enrollment);
        return toResponse(enrollment);
    }

    @Transactional
    public EnrollmentResponse updateEnrollment(Long id, EnrollmentRequest request) {
        Enrollment enrollment = enrollmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment not found with id: " + id));
        if (request.getStatus() != null) {
            enrollment.setStatus(request.getStatus());
        }
        enrollmentRepository.save(enrollment);
        return toResponse(enrollment);
    }

    @Transactional
    public void deleteEnrollment(Long id) {
        Enrollment enrollment = enrollmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment not found with id: " + id));
        enrollmentRepository.delete(enrollment);
    }

    private EnrollmentResponse toResponse(Enrollment enrollment) {
        return EnrollmentResponse.builder()
                .enrollmentId(enrollment.getEnrollmentId())
                .status(enrollment.getStatus())
                .enrolledAt(enrollment.getEnrolledAt())
                .userId(enrollment.getUser().getUserId())
                .username(enrollment.getUser().getUsername())
                .courseId(enrollment.getCourse().getCourseId())
                .courseTitle(enrollment.getCourse().getTitle())
                .build();
    }
}
