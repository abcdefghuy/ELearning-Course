package com.example.Learning_Course_App.mapper;

import com.example.Learning_Course_App.dto.response.ContinueCourseResponse;
import com.example.Learning_Course_App.entity.Course;
import org.springframework.stereotype.Service;

@Service
public class ContinueCourseMapper {
    public ContinueCourseResponse toDTO(Course course, String Category, int progress) {
        return ContinueCourseResponse.builder()
                .courseId(course.getId())
                .courseTitle(course.getCourseName())
                .categoryName(Category)
                .courseImageUrl(course.getDetails().getCourseImg())
                .progress(progress)
                .mentorName(course.getMentor().getMentorName())
                .build();
    }
}
