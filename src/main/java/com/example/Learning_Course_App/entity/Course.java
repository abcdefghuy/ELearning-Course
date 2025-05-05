package com.example.Learning_Course_App.entity;

import com.example.Learning_Course_App.enumeration.CourseStatus;
import com.example.Learning_Course_App.enumeration.Status;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="course")
@Getter
@Setter
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name="course_name", nullable=false)
    private String courseName;

    @Column(name="enabled", nullable=false)
    private boolean enabled;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    private List<CourseCategory> categories;

    @OneToOne(mappedBy = "course", cascade = CascadeType.ALL)
    private CourseDetail details;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    private List<Lesson> lessons;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    private List<Enrollment> enrollments;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BookmarkedCourse> bookmarkedCourses;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    private List<Review> reviews;
    @OneToOne(mappedBy = "course", cascade = CascadeType.ALL)
    private Mentor mentor;
}
