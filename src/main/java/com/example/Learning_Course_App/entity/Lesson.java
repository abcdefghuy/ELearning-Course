package com.example.Learning_Course_App.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "lesson")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "lesson_title", nullable = false)
    private String lessonTitle;
    @Column(name = "lesson_description", nullable = false)
    private String description;
    @Column(name = "created_at", nullable = false, updatable = false)
    private Date createdAt = new Date();
    @Column(name= "duration", nullable = false)
    private Integer duration;
    @Column(name = "update_at", nullable = false)
    private Date updateAt = new Date();
    @Column(name = "lecture_videoUrl", nullable = false)
    private String lectureVideoURL;
    @Column(name = "lecture_order", nullable = false)
    private int lectureOrder;
    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @OneToMany(mappedBy = "lesson", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Assignment> assignments;

    @OneToMany(mappedBy = "lesson", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Progress> progresses;

}
