package com.yenyasoft.online_course_management_system.dtos;

import com.yenyasoft.online_course_management_system.enums.EnrollmentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EnrollmentResponse {
    private Long enrollmentId;
    private EnrollmentStatus status;
    private LocalDateTime enrolledAt;
    private Long userId;
    private String username;
    private Long courseId;
    private String courseTitle;
}
