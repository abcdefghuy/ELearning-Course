package com.example.Learning_Course_App.service;

import com.example.Learning_Course_App.dto.response.QuizQuestionResponse;
import org.springframework.stereotype.Service;

import java.util.List;


public interface IQuizService {
    List<QuizQuestionResponse> getQuizByLessonId(Long lessonId);
}
