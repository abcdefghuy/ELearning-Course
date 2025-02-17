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
    private Long id;

    @OneToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @Column(name = "course_price", nullable = false)
    private Double coursePrice;

    @Column(name = "course_description", length = 1000)
    private String courseDescription;

    @Column(name = "duration")
    private Integer duration; // Số giờ

    @Temporal(TemporalType.DATE)
    @Column(name = "created_at", nullable = false, updatable = false)
    private Date createdAt = new Date(); // Mặc định là ngày hiện tại

    @Column(name = "level", length = 20)
    @Enumerated(EnumType.STRING)
    private CourseLevel level;

    @Column(name = "rating")
    private Double rating = 0.00;

    @Column(name = "student_quantity", columnDefinition = "INT DEFAULT 0")
    private Integer studentQuantity = 0;
}
