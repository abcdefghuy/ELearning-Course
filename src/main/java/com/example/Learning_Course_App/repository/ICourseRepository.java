package com.example.Learning_Course_App.repository;

import com.example.Learning_Course_App.entity.Course;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Repository
public interface ICourseRepository extends JpaRepository<Course, Integer> {
    @Query("SELECT c FROM Course c JOIN c.categories cat WHERE cat.id = :categoryId")
    List<Course> findByCategoryId(@Param("categoryId") Long categoryId);
    @Query("SELECT c FROM Course c WHERE c.courseDetails.createdAt >= :sevenDaysAgo ORDER BY c.courseDetails.createdAt DESC")
    List<Course> findTop10ByCreatedAt(LocalDate sevenDaysAgo, Pageable pageable);
    @Query("SELECT c FROM Course c ORDER BY c.courseDetails.studentQuantity DESC")
    List<Course> findTop10BySoldQuantity(Pageable pageable);
}
