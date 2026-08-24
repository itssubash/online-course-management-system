package com.yenyasoft.online_course_management_system.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CourseResponse {
    private Long courseId;
    private String title;
    private String description;
    private String schedule;
    private List<InstructorResponse> instructors;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
