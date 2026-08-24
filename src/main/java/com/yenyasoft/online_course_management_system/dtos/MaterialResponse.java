package com.yenyasoft.online_course_management_system.dtos;

import com.yenyasoft.online_course_management_system.enums.MaterialType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MaterialResponse {
    private Long materialId;
    private String title;
    private String description;
    private String fileUrl;
    private MaterialType materialType;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long courseId;
    private String courseTitle;
    private Long uploadedByUserId;
    private String uploadedByUsername;
}
