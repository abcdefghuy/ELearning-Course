package com.example.Learning_Course_App.service.Impl;

import com.example.Learning_Course_App.aop.ApiException;
import com.example.Learning_Course_App.entity.Course;
import com.example.Learning_Course_App.entity.Lesson;
import com.example.Learning_Course_App.entity.Progress;
import com.example.Learning_Course_App.entity.User;
import com.example.Learning_Course_App.enumeration.ErrorCode;
import com.example.Learning_Course_App.enumeration.LessonStatus;
import com.example.Learning_Course_App.repository.ILessonRepository;
import com.example.Learning_Course_App.repository.IProgressRepository;
import com.example.Learning_Course_App.service.IProgressService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProgressServiceImpl implements IProgressService {
    private final ILessonRepository lessonRepository;
    private final IProgressRepository progressRepository;
    @Override
    public void initFirstLessonProgress(User student, Course course) {
        Lesson firstLesson = lessonRepository.findFirstByCourseIdOrderByLessonOrderAsc(course.getId())
                .orElseThrow(() -> new ApiException(ErrorCode.LESSON_NOT_FOUND));

        Progress progress = new Progress();
        progress.setLesson(firstLesson);
        progress.setStudent(student);
        progress.setStatus(LessonStatus.UNLOCKED);
        progress.setCreatedAt(new Date());

        progressRepository.save(progress);
    }

    @Override
    public void initLessonProgress(User student, Course course) {
        List<Lesson> lessons = lessonRepository.findByCourseId(course.getId());
        List<Progress> progressList = new ArrayList<>();
        for (int i = 0; i < lessons.size(); i++) {
            Progress progress = new Progress();
            progress.setLesson(lessons.get(i));
            progress.setStudent(student);
            progress.setCreatedAt(new Date());
            if (lessons.get(i).getLessonOrder()==1) {
                progress.setStatus(LessonStatus.UNLOCKED); // Bài học đầu tiên được mở khóa
            } else {
                progress.setStatus(LessonStatus.LOCKED);   // Các bài sau bị khóa
            }
            progressList.add(progress);
        }
        progressRepository.saveAll(progressList);
    }
}
