package com.example.Learning_Course_App.service.Impl;

import com.example.Learning_Course_App.dto.response.LessonResponse;
import com.example.Learning_Course_App.entity.Enrollment;
import com.example.Learning_Course_App.entity.Lesson;
import com.example.Learning_Course_App.entity.Progress;
import com.example.Learning_Course_App.entity.User;
import com.example.Learning_Course_App.enumeration.LessonStatus;
import com.example.Learning_Course_App.enumeration.Status;
import com.example.Learning_Course_App.mapper.LessonMapper;
import com.example.Learning_Course_App.repository.*;
import com.example.Learning_Course_App.service.ILessonService;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import static com.fasterxml.jackson.databind.type.LogicalType.DateTime;

@Service
public class LessonServiceImpl implements ILessonService {
    private final ILessonRepository lessonRepository;
    private final LessonMapper lessonMapper;
    private final IUserRepository userRepository;
    private final IProgressRepository progressRepository;
    private final IEnrollmentRepository enrollmentRepository;
    private final RedisService redisService;

    public LessonServiceImpl(ILessonRepository lessonRepository, LessonMapper lessonMapper, IUserRepository userRepository, IProgressRepository progressRepository, IEnrollmentRepository enrollmentRepository, RedisService redisService) {
        this.lessonRepository = lessonRepository;
        this.lessonMapper = lessonMapper;
        this.userRepository = userRepository;
        this.progressRepository = progressRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.redisService = redisService;
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

        Lesson nextLesson = lessonRepository.findFirstByCourseIdAndLessonOrderGreaterThan(
                        lesson.getCourse().getId(), lesson.getLessonOrder() + 1)
                .orElse(null);  // nextLesson có thể không tồn tại

        createOrUpdateProgress(lesson, student);

        if (nextLesson != null) {
            createOrUpdateProgress(nextLesson, student);
        }

        checkAndCompleteEnrollmentIfNeeded(lesson.getCourse().getId(), student);
    }

    private void checkAndCompleteEnrollmentIfNeeded(Long courseId, User student) {
        List<Lesson> allLessons = lessonRepository.findByCourseId(courseId);

        boolean allUnlocked = allLessons.stream()
                .allMatch(lesson -> progressRepository.existsByLessonAndStudentAndStatus(lesson, student, LessonStatus.UNLOCKED));

        if (allUnlocked) {
            Enrollment enrollment = enrollmentRepository.findByCourseIdAndUserId(courseId, student.getId())
                    .orElseThrow(() -> new RuntimeException("Enrollment not found"));

            if (enrollment.getCourseStatus() != Status.COMPLETED) {
                enrollment.setCourseStatus(Status.COMPLETED);
                enrollmentRepository.save(enrollment);
            }
        }
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
