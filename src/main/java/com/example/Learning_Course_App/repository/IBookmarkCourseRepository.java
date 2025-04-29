package com.example.Learning_Course_App.repository;

import com.example.Learning_Course_App.entity.Bookmark;
import com.example.Learning_Course_App.entity.BookmarkedCourse;
import com.example.Learning_Course_App.entity.Course;
import com.example.Learning_Course_App.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IBookmarkCourseRepository extends JpaRepository<BookmarkedCourse, Long> {
    @Query("SELECT bc.course FROM BookmarkedCourse bc WHERE bc.bookmark.user.id = :userId")
    Page<Course> findCoursesBookmarkedByUserId(@Param("userId") Long userId, Pageable pageable);

    @Query("SELECT COUNT(bc) > 0 FROM BookmarkedCourse bc WHERE bc.bookmark.user.id = :userId AND bc.course.id = :courseId")
    boolean existsByUserIdAndCourseId(@Param("userId") Long userId, @Param("courseId") Long courseId);

    @Query("SELECT bc.course.id FROM BookmarkedCourse bc WHERE bc.bookmark.user.id = :userId AND bc.course.id IN :courseIds")
    List<Long> findBookmarkedCourseIds(@Param("userId") Long userId, @Param("courseIds") List<Long> courseIds);
    @Query("SELECT bc FROM BookmarkedCourse bc WHERE bc.bookmark.user.id = :userId AND bc.course.id = :courseId")
    Optional<BookmarkedCourse> findByUserIdAndCourseId(@Param("userId") Long userId, @Param("courseId") Long courseId);
}
