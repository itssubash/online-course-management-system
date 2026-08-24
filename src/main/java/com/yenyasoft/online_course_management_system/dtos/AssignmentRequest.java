package com.yenyasoft.online_course_management_system.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssignmentRequest {
    private String title;
    private String description;
    private LocalDateTime dueDate;
    private Integer maxScore;
    private Long courseId;
    private Long createdByUserId;
}
