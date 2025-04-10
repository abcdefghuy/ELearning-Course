package com.example.Learning_Course_App.service;

import com.example.Learning_Course_App.dto.response.CourseDetailResponse;
import com.example.Learning_Course_App.entity.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface ICourseService {
    List<CourseDetailResponse> getCourseByCategory(Long categoryId);

    Page<CourseDetailResponse> getTop10BestSellingProducts( Long userId);

    List<CourseDetailResponse> getTop10NewProducts();

    Page<CourseDetailResponse> getAllCourses(PageRequest of, Long userId);
}
