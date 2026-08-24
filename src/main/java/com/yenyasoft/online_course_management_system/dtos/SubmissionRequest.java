package com.yenyasoft.online_course_management_system.dtos;

import com.yenyasoft.online_course_management_system.enums.SubmissionStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubmissionRequest {
    private String content;
    private String fileUrl;
    private Integer score;
    private String feedback;
    private SubmissionStatus submissionStatus;
    private Long assignmentId;
    private Long studentUserId;
}
