package com.yenyasoft.online_course_management_system.repositories;

import com.yenyasoft.online_course_management_system.models.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Long> {
    List<Assignment> findByCourseCourseId(Long courseId);
    List<Assignment> findByCreatedByUserId(Long userId);
}
