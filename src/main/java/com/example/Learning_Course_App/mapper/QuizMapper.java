package com.example.Learning_Course_App.mapper;

import com.example.Learning_Course_App.dto.response.QuizQuestionResponse;
import com.example.Learning_Course_App.entity.QuizQuestion;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuizMapper {
    public QuizQuestionResponse toDTO(QuizQuestion quizQuestion) {
        return QuizQuestionResponse.builder()
                .id(quizQuestion.getQuizId())
                .questionText(quizQuestion.getQuestionText())
                .correctIndex(quizQuestion.getCorrectAnswer())
                .options(List.of(
                        quizQuestion.getOptionA(),
                        quizQuestion.getOptionB(),
                        quizQuestion.getOptionC(),
                        quizQuestion.getOptionD()
                ))
                .lessonId(quizQuestion.getLesson().getId())
                .build();
    }
}
