package com.example.Learning_Course_App.repository;

import com.example.Learning_Course_App.entity.Mentor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface IMentorRepository extends JpaRepository<Mentor, Long> {
    @Query("SELECT m FROM Mentor m JOIN m.course c JOIN c.enrollments e GROUP BY m ORDER BY COUNT(e.user.id) DESC")
    Page<Mentor> findTop10BestMentor(Pageable pageable);
}
