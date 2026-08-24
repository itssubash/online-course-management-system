package com.yenyasoft.online_course_management_system.repositories;

import com.yenyasoft.online_course_management_system.models.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
}
