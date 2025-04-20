package com.example.Learning_Course_App.entity;

import com.example.Learning_Course_App.enumeration.StatusSubmission;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "assignment_submission")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AssignmentSubmission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "submission_text",columnDefinition = "TEXT")
    private String submissionText;
    @Column(name = "submitted_at")
    private LocalDate submittedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private StatusSubmission status = StatusSubmission.Pending;

    @ManyToOne
    @JoinColumn(name = "assignment_id", nullable = false)
    private Assignment assignment;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
