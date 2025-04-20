package com.example.Learning_Course_App.entity;

import com.example.Learning_Course_App.enumeration.CourseLevel;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="course_details")
@Getter
@Setter
public class CourseDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long courseDetailsId;
    @Column(name = "course_img", nullable = false)
    private String courseImg;
    @Column(name = "course_price", nullable = false)
    private double coursePrice;
    @Column(name = "course_description", nullable = false)
    private String courseDescription;
    @Column(name = "level", nullable = false)
    @Enumerated(EnumType.STRING)
    private CourseLevel level;
    @Column(name="created_at", nullable = false)
    private Date createdAt = new Date();
    @Column(name="update_at", nullable = false)
    private Date updateAt = new Date();
    @Column(name="isBestSeller")
    private boolean isBestSeller;
    @Column(name="courseLevel")
    @Enumerated(EnumType.STRING)
    private CourseLevel courseLevel;
    @OneToOne
    @JoinColumn(name = "course_id", nullable = false, unique = true)
    private Course course;
}
