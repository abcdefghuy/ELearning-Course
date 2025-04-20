package com.example.Learning_Course_App.repository;

import com.example.Learning_Course_App.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ICategoryRepository extends JpaRepository<Category, Long> {
    @Query("""
    SELECT cc.category 
    FROM CourseCategory cc 
    WHERE cc.course.id = :courseId
""")
    List<Category> findCategoriesByCourseId(@Param("courseId") Long courseId);
}
