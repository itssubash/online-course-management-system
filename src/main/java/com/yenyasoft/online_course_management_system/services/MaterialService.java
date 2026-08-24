package com.yenyasoft.online_course_management_system.services;

import com.yenyasoft.online_course_management_system.dtos.MaterialRequest;
import com.yenyasoft.online_course_management_system.dtos.MaterialResponse;
import com.yenyasoft.online_course_management_system.exceptions.ResourceNotFoundException;
import com.yenyasoft.online_course_management_system.models.Course;
import com.yenyasoft.online_course_management_system.models.Material;
import com.yenyasoft.online_course_management_system.models.User;
import com.yenyasoft.online_course_management_system.repositories.CourseRepository;
import com.yenyasoft.online_course_management_system.repositories.MaterialRepository;
import com.yenyasoft.online_course_management_system.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MaterialService {

    private final MaterialRepository materialRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    public MaterialService(MaterialRepository materialRepository, CourseRepository courseRepository,
                           UserRepository userRepository) {
        this.materialRepository = materialRepository;
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
    }

    public List<MaterialResponse> getAllMaterials() {
        return materialRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public MaterialResponse getMaterialById(Long id) {
        Material material = materialRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Material not found with id: " + id));
        return toResponse(material);
    }

    public List<MaterialResponse> getMaterialsByCourseId(Long courseId) {
        return materialRepository.findByCourseCourseId(courseId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<MaterialResponse> getMaterialsByUserId(Long userId) {
        return materialRepository.findByUploadedByUserId(userId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public MaterialResponse createMaterial(MaterialRequest request) {
        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + request.getCourseId()));
        User user = userRepository.findById(request.getUploadedByUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + request.getUploadedByUserId()));

        Material material = Material.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .fileUrl(request.getFileUrl())
                .materialType(request.getMaterialType())
                .course(course)
                .uploadedBy(user)
                .build();

        materialRepository.save(material);
        return toResponse(material);
    }

    @Transactional
    public MaterialResponse updateMaterial(Long id, MaterialRequest request) {
        Material material = materialRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Material not found with id: " + id));

        if (request.getTitle() != null) material.setTitle(request.getTitle());
        if (request.getDescription() != null) material.setDescription(request.getDescription());
        if (request.getFileUrl() != null) material.setFileUrl(request.getFileUrl());
        if (request.getMaterialType() != null) material.setMaterialType(request.getMaterialType());

        if (request.getCourseId() != null) {
            Course course = courseRepository.findById(request.getCourseId())
                    .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + request.getCourseId()));
            material.setCourse(course);
        }
        if (request.getUploadedByUserId() != null) {
            User user = userRepository.findById(request.getUploadedByUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + request.getUploadedByUserId()));
            material.setUploadedBy(user);
        }

        materialRepository.save(material);
        return toResponse(material);
    }

    @Transactional
    public void deleteMaterial(Long id) {
        Material material = materialRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Material not found with id: " + id));
        materialRepository.delete(material);
    }

    private MaterialResponse toResponse(Material material) {
        return MaterialResponse.builder()
                .materialId(material.getMaterialId())
                .title(material.getTitle())
                .description(material.getDescription())
                .fileUrl(material.getFileUrl())
                .materialType(material.getMaterialType())
                .createdAt(material.getCreatedAt())
                .updatedAt(material.getUpdatedAt())
                .courseId(material.getCourse().getCourseId())
                .courseTitle(material.getCourse().getTitle())
                .uploadedByUserId(material.getUploadedBy().getUserId())
                .uploadedByUsername(material.getUploadedBy().getUsername())
                .build();
    }
}
