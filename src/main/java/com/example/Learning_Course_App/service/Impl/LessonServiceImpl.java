package com.example.Learning_Course_App.service.Impl;

import com.example.Learning_Course_App.dto.response.LessonResponse;
import com.example.Learning_Course_App.entity.Lesson;
import com.example.Learning_Course_App.entity.Progress;
import com.example.Learning_Course_App.entity.User;
import com.example.Learning_Course_App.enumeration.LessonStatus;
import com.example.Learning_Course_App.mapper.LessonMapper;
import com.example.Learning_Course_App.repository.ICourseRepository;
import com.example.Learning_Course_App.repository.ILessonRepository;
import com.example.Learning_Course_App.repository.IProgressRepository;
import com.example.Learning_Course_App.repository.IUserRepository;
import com.example.Learning_Course_App.service.ILessonService;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;

import static com.fasterxml.jackson.databind.type.LogicalType.DateTime;

@Service
public class LessonServiceImpl implements ILessonService {
    private final ILessonRepository lessonRepository;
    private final LessonMapper lessonMapper;
    private final IUserRepository userRepository;
    private final IProgressRepository progressRepository;

    public LessonServiceImpl(ILessonRepository lessonRepository, LessonMapper lessonMapper, IUserRepository userRepository, IProgressRepository progressRepository) {
        this.lessonRepository = lessonRepository;
        this.lessonMapper = lessonMapper;
        this.userRepository = userRepository;
        this.progressRepository = progressRepository;
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
            LessonResponse dto = lessonMapper.toDTO(lesson, status);
            System.out.println("Lesson " + lesson.getId() + " | hasQuiz: " + dto.isHasQuiz()); // debug tại đây
            return dto;
        });
    }
    public Page<LessonResponse> getLessonsDemoByCourse(Long courseId, Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page,size, Sort.by(Sort.Direction.ASC, "lessonOrder"));
        Page<Lesson> lessons = lessonRepository.findByCourseIdOrderByLectureOrderAsc(courseId, pageable);
        return lessons.map(lesson -> {
            String status;
            if (lesson.getLessonOrder() == 1) {
                status = "UNLOCKED";
            }
            else {
                status = "LOCKED";
            }
            return lessonMapper.toDTO(lesson, status);
        });
    }


    @Override
    @Transactional
    public void updateProgress(Long lessonId, Long userId) {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new RuntimeException("Lesson not found"));

        User student = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        // Tìm bài học tiếp theo
        Lesson nextLesson = lessonRepository.findFirstByCourseIdAndLessonOrderGreaterThan(lesson.getLessonOrder(), lesson.getCourse().getId())
                .orElseThrow(() -> new RuntimeException("Next lesson not found"));

        // Cập nhật progress cho bài học hiện tại
        createOrUpdateProgress(lesson, student);

        // Cập nhật progress cho bài học tiếp theo nếu chưa có
        createOrUpdateProgress(nextLesson, student);
    }

    private void createOrUpdateProgress(Lesson lesson, User student) {
        // Kiểm tra và tạo mới hoặc cập nhật Progress
        Progress progress = progressRepository.findByLessonAndStudent(lesson, student)
                .orElseGet(() -> {
                    Progress newProgress = new Progress();
                    newProgress.setLesson(lesson);
                    newProgress.setStudent(student);
                    return newProgress;
                });

        progress.setStatus(LessonStatus.UNLOCKED);
        progress.setCreatedAt(new Date()); // Gán ngày tạo
        progressRepository.save(progress);
    }
}
