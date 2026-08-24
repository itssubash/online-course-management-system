package com.yenyasoft.online_course_management_system.dtos;

import com.yenyasoft.online_course_management_system.enums.SubmissionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SubmissionResponse {
    private Long submissionId;
    private String content;
    private String fileUrl;
    private LocalDateTime submittedAt;
    private Integer score;
    private String feedback;
    private SubmissionStatus submissionStatus;
    private Long assignmentId;
    private String assignmentTitle;
    private Long studentUserId;
    private String studentUsername;
}
