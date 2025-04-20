package com.example.Learning_Course_App.entity;

import com.example.Learning_Course_App.enumeration.Status;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "enrollment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Enrollment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name="status", nullable=false)
    @Enumerated(EnumType.STRING)
    private Status courseStatus;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;
}
