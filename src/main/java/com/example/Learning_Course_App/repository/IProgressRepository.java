package com.example.Learning_Course_App.repository;

import com.example.Learning_Course_App.entity.Lesson;
import com.example.Learning_Course_App.entity.Progress;
import com.example.Learning_Course_App.entity.User;
import com.example.Learning_Course_App.enumeration.LessonStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IProgressRepository extends JpaRepository<Progress, Long> {
    @Query("SELECT p FROM Progress p WHERE p.lesson = ?1 AND p.student = ?2")
    Optional<Progress> findByLessonAndStudent(Lesson lesson, User student);

    boolean existsByLessonAndStudentAndStatus(Lesson lesson, User student, LessonStatus status);
    // Custom query methods can be defined here if needed
    // For example, to find progress by lesson and user
    // Optional<Progress> findByLessonAndStudent(Lesson lesson, User student);
}
