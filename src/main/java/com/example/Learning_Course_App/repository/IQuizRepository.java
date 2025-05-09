package com.example.Learning_Course_App.repository;

import com.example.Learning_Course_App.entity.QuizQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IQuizRepository extends JpaRepository<QuizQuestion, Long> {
    List<QuizQuestion> findByLessonId(Long lessonId);
}
