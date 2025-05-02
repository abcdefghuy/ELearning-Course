package com.example.Learning_Course_App.service;

import com.example.Learning_Course_App.entity.Course;
import com.example.Learning_Course_App.entity.User;

public interface IEnrollmentService {
    void enrollUser(User student, Course course);
}
