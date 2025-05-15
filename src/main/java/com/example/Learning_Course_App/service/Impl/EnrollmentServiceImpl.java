package com.example.Learning_Course_App.service.Impl;

import com.example.Learning_Course_App.entity.Course;
import com.example.Learning_Course_App.entity.Enrollment;
import com.example.Learning_Course_App.entity.User;
import com.example.Learning_Course_App.enumeration.Status;
import com.example.Learning_Course_App.repository.IEnrollmentRepository;
import com.example.Learning_Course_App.service.IEnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EnrollmentServiceImpl implements IEnrollmentService {
    private final IEnrollmentRepository repository;
    private final RedisService redisService;

    @Autowired
    public EnrollmentServiceImpl(IEnrollmentRepository repository, RedisService redisService) {
        this.repository = repository;
        this.redisService = redisService;
    }

    @Override
    public void enrollUser(User student, Course course) {
        boolean exists = repository.existsByUserIdAndCourseId(student.getId(), course.getId());
        if (!exists) {
            Enrollment enrollment = new Enrollment();
            enrollment.setUser(student);
            enrollment.setCourse(course);
            enrollment.setCourseStatus(Status.IN_PROGRESS);
            repository.save(enrollment);
        }
    }
}
