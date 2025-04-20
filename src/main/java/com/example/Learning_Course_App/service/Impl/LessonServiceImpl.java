package com.example.Learning_Course_App.service.Impl;

import com.example.Learning_Course_App.dto.response.LessonResponse;
import com.example.Learning_Course_App.entity.Lesson;
import com.example.Learning_Course_App.mapper.LessonMapper;
import com.example.Learning_Course_App.repository.ILessonRepository;
import com.example.Learning_Course_App.service.ILessonService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LessonServiceImpl implements ILessonService {
    private final ILessonRepository lessonRepository;
    private final LessonMapper lessonMapper;

    public LessonServiceImpl(ILessonRepository lessonRepository, LessonMapper lessonMapper) {
        this.lessonRepository = lessonRepository;
        this.lessonMapper = lessonMapper;
    }

    public Page<LessonResponse> getLessonsByCourse(Long courseId, Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page,size, Sort.by(Sort.Direction.ASC, "lessonOrder"));
        Page<Lesson> lessons = lessonRepository.findByCourseIdOrderByLectureOrderAsc(courseId, pageable);

        return lessons.map(lesson -> {
            String status;

            if (userId == null) {
                status = lesson.getLessonOrder() == 1 ? "UNLOCKED" : "LOCKED";
            } else {
                status = lesson.getProgresses().stream()
                        .filter(progress -> progress.getStudent().getId().equals(userId))
                        .findFirst()
                        .map(p -> p.getStatus().toString())
                        .orElse("LOCKED");
            }

            return lessonMapper.toDTO(lesson, status);
        });
    }
}
