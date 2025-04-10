package com.example.Learning_Course_App.controller;

import com.example.Learning_Course_App.dto.response.CourseDetailResponse;
import com.example.Learning_Course_App.service.ICourseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/courses")
public class CourseController {
    private final ICourseService courseService;
    public CourseController(ICourseService courseService) {
        this.courseService = courseService;
    }
    // API: Lấy 10 sản phẩm bán chạy nhất
    @GetMapping("/top-selling")
    public ResponseEntity<?> getTop10BestSellingProducts(@RequestParam(required = false) Long userId) {
        Page<CourseDetailResponse> courses = courseService.getTop10BestSellingProducts(userId);
        Map<String, Object> response = new HashMap<>();
        response.put("courses", courses.getContent());
        response.put("totalPages", courses.getTotalPages());
        response.put("currentPage", courses.getNumber());
        response.put("pageSize", courses.getSize());
        response.put("totalElements", courses.getTotalElements());
        response.put("last", courses.isLast());
        return ResponseEntity.ok(response);
    }
    @GetMapping("/{categoryId}/courses")
    public ResponseEntity<List<CourseDetailResponse>> getCourseByCategory(@PathVariable Long categoryId) {
        List<CourseDetailResponse> products = courseService.getCourseByCategory(categoryId);
        return ResponseEntity.ok(products);
    }
    @GetMapping("/all-courses")
    public ResponseEntity<Map<String, Object>> getAllCourses(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size, @RequestParam(required = false) Long userId) {

        Page<CourseDetailResponse> allCourses = courseService.getAllCourses(PageRequest.of(page, size), userId);
        Map<String, Object> response = new HashMap<>();
        response.put("courses", allCourses.getContent());
        response.put("totalPages", allCourses.getTotalPages());
        response.put("currentPage", allCourses.getNumber());
        response.put("pageSize", allCourses.getSize());
        response.put("totalElements", allCourses.getTotalElements());
        response.put("last", allCourses.isLast());
        return ResponseEntity.ok(response);
    }


    // API: Lấy 10 sản phẩm mới nhất trong vòng 7 ngày
    @GetMapping("/newest")
    public ResponseEntity<List<CourseDetailResponse>> getTop10NewProducts() {
        return ResponseEntity.ok(courseService.getTop10NewProducts());
    }
}
