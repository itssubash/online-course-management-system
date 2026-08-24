package com.yenyasoft.online_course_management_system.repositories;

import com.yenyasoft.online_course_management_system.models.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    List<Enrollment> findByUserUserId(Long userId);
    List<Enrollment> findByCourseCourseId(Long courseId);
    Optional<Enrollment> findByUserUserIdAndCourseCourseId(Long userId, Long courseId);
    boolean existsByUserUserIdAndCourseCourseId(Long userId, Long courseId);
}
