package com.yenyasoft.online_course_management_system.dtos;

import com.yenyasoft.online_course_management_system.enums.MaterialType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MaterialRequest {
    private String title;
    private String description;
    private String fileUrl;
    private MaterialType materialType;
    private Long courseId;
    private Long uploadedByUserId;
}
