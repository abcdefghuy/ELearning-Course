package com.example.Learning_Course_App.service;

import com.example.Learning_Course_App.dto.response.CourseDetailResponse;
import com.example.Learning_Course_App.entity.Course;

import java.util.List;

public interface ICourseService {
    List<CourseDetailResponse> getCourseByCategory(Long categoryId);

    List<CourseDetailResponse> getTop10BestSellingProducts();

    List<CourseDetailResponse> getTop10NewProducts();
}
