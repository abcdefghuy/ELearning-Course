package com.example.Learning_Course_App.mapper;

import com.example.Learning_Course_App.dto.response.CourseDetailResponse;
import com.example.Learning_Course_App.dto.response.LessonResponse;
import com.example.Learning_Course_App.dto.response.ReviewResponse;
import com.example.Learning_Course_App.entity.Course;
import com.example.Learning_Course_App.entity.CourseDetail;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseDetailMapper {
    public CourseDetailResponse toDTO(Course course, boolean isEnrolled) {
        return CourseDetailResponse.builder()
                .courseId(course.getId())
                .courseName(course.getCourseName())
                .coursePrice(course.getDetails().getCoursePrice())
                .courseDescription(course.getDetails().getCourseDescription())
                .courseImg(course.getDetails().getCourseImg())
                .enabled(course.getDetails().getCourse().isEnabled())
                .createdAt(course.getDetails().getCreatedAt())
                .updateAt(course.getDetails().getUpdateAt())
                .level(course.getDetails().getLevel().name())
                .categoryNames(
                        course.getCategories().stream()
                                .map(cc -> cc.getCategory().getCategoryName())
                                .toList()
                )
                .reviewCount(course.getReviews().size())
                .lessonCount(course.getLessons().size())
                .isBestSeller(course.getDetails().isBestSeller())
                .studentQuantity(course.getEnrollments().size())
                .isEnrolled(isEnrolled)
                .build();
    }
}
