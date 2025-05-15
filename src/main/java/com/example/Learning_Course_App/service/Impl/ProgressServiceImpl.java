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
    public void initCourseProgress(User student, Course course) {
        // Lấy tất cả lesson trong khóa học, sắp xếp theo thứ tự
        List<Lesson> lessons = lessonRepository.findAllByCourseIdOrderByLessonOrderAsc(course.getId());

        if (lessons.isEmpty()) {
            throw new ApiException(ErrorCode.LESSON_NOT_FOUND);
        }

        List<Progress> progressList = new ArrayList<>();
        Date now = new Date();

        for (int i = 0; i < lessons.size(); i++) {
            Lesson lesson = lessons.get(i);

            Progress progress = new Progress();
            progress.setLesson(lesson);
            progress.setStudent(student);
            progress.setCreatedAt(now);

            // Lesson đầu tiên được mở khóa
            if (i == 0) {
                progress.setStatus(LessonStatus.UNLOCKED);
            } else {
                progress.setStatus(LessonStatus.LOCKED);
            }

            progressList.add(progress);
        }

        progressRepository.saveAll(progressList);
    }
}
