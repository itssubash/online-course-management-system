package com.yenyasoft.online_course_management_system.repositories;

import com.yenyasoft.online_course_management_system.models.Material;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MaterialRepository extends JpaRepository<Material, Long> {
    List<Material> findByCourseCourseId(Long courseId);
    List<Material> findByUploadedByUserId(Long userId);
}
