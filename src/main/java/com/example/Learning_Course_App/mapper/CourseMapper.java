package com.example.Learning_Course_App.mapper;

import com.example.Learning_Course_App.dto.response.CourseResponse;
import com.example.Learning_Course_App.entity.Course;
import com.example.Learning_Course_App.entity.CourseDetail;
import org.springframework.stereotype.Service;

@Service
public class CourseMapper {
    public CourseResponse toDTO(Course course, boolean isBookmarked) {
        return CourseResponse.builder()
                .courseId(course.getId())
                .courseName(course.getCourseName())
                .enabled(course.isEnabled())
                .coursePrice(course.getDetails().getCoursePrice())
                .courseImg(course.getDetails().getCourseImg())
                .isBookmarked(isBookmarked)
                .isBestSeller(course.getDetails().isBestSeller())
                .build();
    }
}
