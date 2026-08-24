package com.yenyasoft.online_course_management_system.dtos;

import com.yenyasoft.online_course_management_system.enums.EnrollmentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EnrollmentRequest {
    private Long userId;
    private Long courseId;
    private EnrollmentStatus status;
}
