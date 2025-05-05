package com.example.Learning_Course_App.mapper;

import com.example.Learning_Course_App.dto.response.CourseResponse;
import com.example.Learning_Course_App.entity.Course;
import com.example.Learning_Course_App.entity.Review;
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
                .rating(
                        course.getReviews().stream()
                                .mapToInt(Review::getRating)
                                .average()
                                .orElse(0.0)
                )
                .mentorName(course.getMentor().getMentorName())
                .mentorAvatar(course.getMentor().getMentorAvatar())
                .build();
    }
}
