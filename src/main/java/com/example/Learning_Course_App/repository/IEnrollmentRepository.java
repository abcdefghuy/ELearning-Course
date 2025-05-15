package com.example.Learning_Course_App.repository;

import com.example.Learning_Course_App.entity.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IEnrollmentRepository extends JpaRepository<Enrollment, Long> {
    boolean existsByUserIdAndCourseId(Long userId, long courseId);


    Optional<Enrollment> findByCourseIdAndUserId(Long courseId, Long id);
}
