package com.example.Learning_Course_App.repository;

import com.example.Learning_Course_App.entity.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IBookMarkRepository extends JpaRepository<Bookmark, Long> {
    @Query("SELECT b.course.id FROM Bookmark b WHERE b.user.id = :userId AND b.course.id IN :courseIds")
    List<Long> findBookmarkedCourseIdsByUserIdAndCourseIds(@Param("userId") Long userId, @Param("courseIds") List<Long> courseIds) ;
}
