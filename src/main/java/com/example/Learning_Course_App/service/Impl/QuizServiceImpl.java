package com.example.Learning_Course_App.service.Impl;

import com.example.Learning_Course_App.dto.response.QuizQuestionResponse;
import com.example.Learning_Course_App.mapper.QuizMapper;
import com.example.Learning_Course_App.repository.IQuizRepository;
import com.example.Learning_Course_App.service.IQuizService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class QuizServiceImpl implements IQuizService {
     private final IQuizRepository quizRepository;
     private RedisService redisService;
     private final QuizMapper quizMapper;
    public QuizServiceImpl(IQuizRepository quizRepository, QuizMapper quizMapper, RedisService redisService) {
        this.quizRepository = quizRepository;
        this.quizMapper = quizMapper;
        this.redisService = redisService;
    }
    @Override
    public List<QuizQuestionResponse> getQuizByLessonId(Long lessonId) {
        redisService.delete("quiz_" + lessonId);
        String redisKey = "quiz_" + lessonId;
        List<QuizQuestionResponse> quizQuestions = redisService.getList(redisKey, QuizQuestionResponse.class);
        if (quizQuestions == null) {
            quizQuestions = quizRepository.findByLessonId(lessonId).stream()
                    .map(quizMapper::toDTO)
                    .toList();
            redisService.saveList(redisKey, quizQuestions, 60);
        }
        return quizQuestions;
    }
}
