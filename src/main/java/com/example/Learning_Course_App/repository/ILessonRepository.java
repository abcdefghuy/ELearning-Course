package com.example.Learning_Course_App.repository;

import com.example.Learning_Course_App.entity.Lesson;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ILessonRepository extends JpaRepository<Lesson, Long> {
    @Query("SELECT l FROM Lesson l WHERE l.course.id = :courseId ORDER BY l.lessonOrder ASC")
    Page<Lesson> findByCourseIdOrderByLectureOrderAsc(@Param("courseId") Long courseId, Pageable pageable);
    @Query("SELECT l FROM Lesson l WHERE l.course.id = :courseId AND l.lessonOrder > :lessonOrder ORDER BY l.lessonOrder ASC")
    Optional<Lesson> findFirstByCourseIdAndLessonOrderGreaterThan(@Param("courseId") int courseId,
                                                                  @Param("lessonOrder") Long lessonOrder);
}
