package com.example.Learning_Course_App.repository;

import com.example.Learning_Course_App.entity.Bookmark;
import com.example.Learning_Course_App.entity.Course;
import com.example.Learning_Course_App.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IBookMarkRepository extends JpaRepository<Bookmark, Long> {
    @Query("SELECT b.course.id FROM Bookmark b WHERE b.user.id = :userId AND b.course.id IN :courseIds")
    List<Long> findBookmarkedCourseIdsByUserIdAndCourseIds(@Param("userId") Long userId, @Param("courseIds") List<Long> courseIds) ;
    @Query("SELECT b.course FROM Bookmark b WHERE b.user.id = :userId")
    Page<Course> findCoursesBookmarkedByUser(@Param("userId") Long userId, Pageable pageable);
    @Query("SELECT COUNT(b) > 0 FROM Bookmark b WHERE b.user.id = :userId AND b.course.id = :courseId")
    boolean existsByUserAndCourse(Long userId, Long courseId);
}
