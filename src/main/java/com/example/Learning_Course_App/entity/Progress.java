package com.example.Learning_Course_App.entity;

import com.example.Learning_Course_App.enumeration.LessonStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "progress")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Progress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "lessonStatus")
    @Enumerated(EnumType.STRING)
    private LessonStatus status;
    private Date createdAt ;

    private Date updateAt ;

    @ManyToOne
    @JoinColumn(name = "lesson_id")
    private Lesson lesson;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private User student;
}
