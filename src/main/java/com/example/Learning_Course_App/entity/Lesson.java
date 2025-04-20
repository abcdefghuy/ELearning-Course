package com.example.Learning_Course_App.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "lesson")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "lesson_title", nullable = false)
    private String lessonTitle;
    @Column(name = "lesson_description", nullable = false)
    private String description;
    @Column(name = "created_at", nullable = false, updatable = false)
    private Date createdAt = new Date();
    @Column(name= "duration", nullable = false)
    private int duration;
    @Column(name = "update_at", nullable = false)
    private Date updateAt = new Date();
    @Column(name = "lesson_videoUrl", nullable = false)
    private String lectureVideoURL;
    @Column(name = "lesson_order", nullable = false)
    private int lessonOrder;
    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @OneToMany(mappedBy = "lesson", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Assignment> assignments;

    @OneToMany(mappedBy = "lesson", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Progress> progresses;

}
