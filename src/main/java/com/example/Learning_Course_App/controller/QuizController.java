package com.example.Learning_Course_App.controller;

import com.example.Learning_Course_App.dto.response.ApiResponse;
import com.example.Learning_Course_App.dto.response.QuizQuestionResponse;
import com.example.Learning_Course_App.enumeration.ErrorCode;
import com.example.Learning_Course_App.service.IQuizService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class QuizController {
    private final IQuizService quizService;

    public QuizController(IQuizService quizService) {
        this.quizService = quizService;
    }

    @GetMapping("/api/lessons/{lessonId}/quiz")
    public ResponseEntity<?> getQuiz(@PathVariable Long lessonId) {
        List<QuizQuestionResponse> quizQuestions = quizService.getQuizByLessonId(lessonId);
        return ResponseEntity.ok(ApiResponse.success(ErrorCode.SUCCESS, quizQuestions));
    }
}
