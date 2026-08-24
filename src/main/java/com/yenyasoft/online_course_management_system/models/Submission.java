package com.yenyasoft.online_course_management_system.models;

import com.yenyasoft.online_course_management_system.enums.SubmissionStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "submission_table")
public class Submission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long submissionId;
    private String content;
    private String fileUrl;
    private LocalDateTime submittedAt;
    private Integer score;
    private String feedback;
    @Enumerated(EnumType.STRING)
    private SubmissionStatus submissionStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assignment_id", nullable = false)
    @ToString.Exclude
    private Assignment assignment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    @ToString.Exclude
    private User student;

    @PrePersist
    public void onCreate() {
        this.submittedAt = LocalDateTime.now();
    }
}
