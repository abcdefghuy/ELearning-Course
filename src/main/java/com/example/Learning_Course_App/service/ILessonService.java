package com.example.Learning_Course_App.service;

import com.example.Learning_Course_App.dto.response.LessonResponse;
import org.springframework.data.domain.Page;

public interface ILessonService {
    public Page<LessonResponse> getLessonsByCourse(Long courseId, Long userId, int page, int size);
}
