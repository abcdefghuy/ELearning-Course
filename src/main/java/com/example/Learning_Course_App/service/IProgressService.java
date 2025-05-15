package com.example.Learning_Course_App.service;

import com.example.Learning_Course_App.entity.Course;
import com.example.Learning_Course_App.entity.User;

public interface IProgressService {
    void initFirstLessonProgress(User student, Course course);
    void initLessonProgress(User student, Course course);
}
