package com.example.Learning_Course_App.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "bookmarked_course")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookmarkedCourse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "bookmark_id", nullable = false)
    private Bookmark bookmark;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Column(name = "created_at")
    private LocalDate createdAt;
}
