package com.example.Learning_Course_App.entity;

import com.example.Learning_Course_App.enumeration.CourseLevel;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="course_details")
@Getter
@Setter
public class CourseDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer courseDetailsId;
    @Column(name = "course_img", nullable = false)
    private String courseImg;
    @Column(name = "course_price", nullable = false)
    private double coursePrice;
    @Column(name = "course_description", nullable = false)
    private String courseDescription;
    @Column(name="created_at", nullable = false)
    private Date createdAt = new Date();
    @Column(name="update_at", nullable = false)
    private Date updateAt = new Date();
    @Column(name="isBestSeller")
    private boolean isBestSeller;
    @OneToOne
    @JoinColumn(name = "course_id", nullable = false, unique = true)
    private Course course;
}
