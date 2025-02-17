package com.example.Learning_Course_App.controller;

import com.example.Learning_Course_App.dto.response.CourseDetailResponse;
import com.example.Learning_Course_App.service.ICourseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/course")
public class CourseController {
    private final ICourseService courseService;
    public CourseController(ICourseService courseService) {
        this.courseService = courseService;
    }
    // API: Lấy 10 sản phẩm bán chạy nhất
    @GetMapping("/top-selling")
    public ResponseEntity<List<CourseDetailResponse>> getTop10BestSellingProducts() {
        return ResponseEntity.ok(courseService.getTop10BestSellingProducts());
    }

    // API: Lấy 10 sản phẩm mới nhất trong vòng 7 ngày
    @GetMapping("/newest")
    public ResponseEntity<List<CourseDetailResponse>> getTop10NewProducts() {
        return ResponseEntity.ok(courseService.getTop10NewProducts());
    }
}
