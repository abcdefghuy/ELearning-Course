package com.example.Learning_Course_App.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "course_category")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CourseCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
}
