package com.example.Learning_Course_App.mapper;

import com.example.Learning_Course_App.dto.response.LessonResponse;
import com.example.Learning_Course_App.entity.Lesson;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

@Service
public class LessonMapper {
    public LessonResponse toDTO(Lesson lesson, String status) {
        return LessonResponse.builder()
                .lessonId(lesson.getId())
                .lessonDescription(lesson.getDescription())
                .lessonName(lesson.getLessonTitle())
                .lessonVideoUrl(lesson.getLectureVideoURL())
                .status(status)
                .lessonOrder(lesson.getLessonOrder())
                .courseId(lesson.getCourse().getId())
                .duration(lesson.getDuration())
                .isHasQuiz(lesson.getQuizQuestions() != null && !lesson.getQuizQuestions().isEmpty())
                .build();
    }
}
