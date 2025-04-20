package com.example.Learning_Course_App.service;

import com.example.Learning_Course_App.dto.response.ContinueCourseResponse;
import com.example.Learning_Course_App.dto.response.CourseDetailResponse;
import com.example.Learning_Course_App.dto.response.CourseResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface ICourseService {
    Page<CourseResponse> getCourseByCategory(Long categoryId, int page, int size, Long userId);

    Page<CourseResponse> getTop10BestSellingProducts( Long userId);

    List<CourseDetailResponse> getTop10NewProducts();

    Page<CourseResponse> getAllCourses(PageRequest of, Long userId);
    CourseDetailResponse getDetailCourse(Long courseId, Long userId);

    ContinueCourseResponse getLatestContinueCourse(Long userId);

    Page<ContinueCourseResponse> getAllContinueCourses(Long userId, PageRequest of);

    Page<ContinueCourseResponse> getAllContinueCoursesCompleted(Long userId, PageRequest of);
}
