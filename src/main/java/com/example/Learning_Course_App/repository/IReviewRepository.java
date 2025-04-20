package com.example.Learning_Course_App.repository;

import com.example.Learning_Course_App.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IReviewRepository extends JpaRepository<Review, Long> {
    @Query("select r from Review r where r.course.id = :courseId ")
    Page<Review> getReviewsByCourse(Pageable pageable, @Param("courseId") Long courseId);
}
