package com.yenyasoft.online_course_management_system.services;

import com.yenyasoft.online_course_management_system.dtos.CourseRequest;
import com.yenyasoft.online_course_management_system.dtos.CourseResponse;
import com.yenyasoft.online_course_management_system.dtos.InstructorResponse;
import com.yenyasoft.online_course_management_system.exceptions.ResourceNotFoundException;
import com.yenyasoft.online_course_management_system.models.Course;
import com.yenyasoft.online_course_management_system.models.User;
import com.yenyasoft.online_course_management_system.repositories.CourseRepository;
import com.yenyasoft.online_course_management_system.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CourseService {

    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    public CourseService(CourseRepository courseRepository, UserRepository userRepository) {
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
    }

    public List<CourseResponse> getAllCourses() {
        return courseRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public CourseResponse getCourseById(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + id));
        return toResponse(course);
    }

    @Transactional
    public CourseResponse createCourse(CourseRequest request) {
        Course course = Course.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .schedule(request.getSchedule())
                .build();

        if (request.getInstructorIds() != null && !request.getInstructorIds().isEmpty()) {
            Set<User> instructors = new HashSet<>(userRepository.findAllById(request.getInstructorIds()));
            course.setInstructors(instructors);
        }

        courseRepository.save(course);
        return toResponse(course);
    }

    @Transactional
    public CourseResponse updateCourse(Long id, CourseRequest request) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + id));

        if (request.getTitle() != null) course.setTitle(request.getTitle());
        if (request.getDescription() != null) course.setDescription(request.getDescription());
        if (request.getSchedule() != null) course.setSchedule(request.getSchedule());

        if (request.getInstructorIds() != null) {
            Set<User> instructors = new HashSet<>(userRepository.findAllById(request.getInstructorIds()));
            course.setInstructors(instructors);
        }

        courseRepository.save(course);
        return toResponse(course);
    }

    @Transactional
    public void deleteCourse(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + id));
        courseRepository.delete(course);
    }

    private CourseResponse toResponse(Course course) {
        List<InstructorResponse> instructors = course.getInstructors().stream()
                .map(user -> InstructorResponse.builder()
                        .userId(user.getUserId())
                        .username(user.getUsername())
                        .email(user.getEmail())
                        .firstName(user.getFirstName())
                        .lastName(user.getLastName())
                        .build())
                .collect(Collectors.toList());

        return CourseResponse.builder()
                .courseId(course.getCourseId())
                .title(course.getTitle())
                .description(course.getDescription())
                .schedule(course.getSchedule())
                .instructors(instructors)
                .createdAt(course.getCreatedAt())
                .updatedAt(course.getUpdatedAt())
                .build();
    }
}
