package com.yenyasoft.online_course_management_system.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InstructorResponse {
    private Long userId;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
}
