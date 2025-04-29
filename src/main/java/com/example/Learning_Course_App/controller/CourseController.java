package com.example.Learning_Course_App.controller;

import com.example.Learning_Course_App.dto.response.*;
import com.example.Learning_Course_App.entity.User;
import com.example.Learning_Course_App.enumeration.ErrorCode;
import com.example.Learning_Course_App.service.ICourseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
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
    public ResponseEntity<?> getTop10BestSellingProducts(@AuthenticationPrincipal User user) {
        Long userId = user != null ? user.getId() : null;
        Page<CourseResponse> courses = courseService.getTop10BestSellingProducts(userId);
        PagedResponse<CourseResponse> response = new PagedResponse<>(
                courses.getContent(), courses.getNumber(), courses.getSize(),
                courses.getTotalElements(), courses.getTotalPages(), courses.isLast()
        );
        return ResponseEntity.ok(ApiResponse.success(ErrorCode.SUCCESS, response));
    }
    @GetMapping("/{categoryName}/courses")
    public ResponseEntity<?> getCourseByCategory(
            @PathVariable String categoryName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @AuthenticationPrincipal User user
    ) {
        Long userId = user != null ? user.getId() : null;
        Page<CourseResponse> courses = courseService.getCourseByCategory(categoryName, page, size, userId);
        PagedResponse<CourseResponse> response = new PagedResponse<>(
                courses.getContent(),
                courses.getNumber(),
                courses.getSize(),
                courses.getTotalElements(),
                courses.getTotalPages(),
                courses.isLast()
        );
        return ResponseEntity.ok(ApiResponse.success(ErrorCode.SUCCESS, response));
    }

    @GetMapping("/all-courses")
    public ResponseEntity<?> getAllCourses(@AuthenticationPrincipal User user,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Long userId = user != null ? user.getId() : null;
        Page<CourseResponse> allCourses = courseService.getAllCourses(PageRequest.of(page, size), userId);
        PagedResponse<CourseResponse> response = new PagedResponse<>(
                allCourses.getContent(),   allCourses.getNumber(), allCourses.getSize(),
                allCourses.getTotalElements(), allCourses.getTotalPages(),   allCourses.isLast()
        );
        return ResponseEntity.ok(ApiResponse.success(ErrorCode.SUCCESS, response));
    }
    // API: Lấy thông tin chi tiết của một sản phẩm
    @GetMapping("/{courseId}")
    public ResponseEntity<?> getCourseDetail(@PathVariable Long courseId, @RequestParam(required = false) Long userId) {
        CourseDetailResponse courseDetail = courseService.getDetailCourse(courseId, userId);
        return ResponseEntity.ok(ApiResponse.success(ErrorCode.SUCCESS, courseDetail));
    }

    // API: Lấy 10 sản phẩm mới nhất trong vòng 7 ngày
    @GetMapping("/newest")
    public ResponseEntity<List<CourseDetailResponse>> getTop10NewProducts() {
        return ResponseEntity.ok(courseService.getTop10NewProducts());
    }
    @GetMapping("/continue/latest")
    public ResponseEntity<?> getLatestContinueCourse(@AuthenticationPrincipal User user) {
        Long userId = user != null ? user.getId() : null;
        ContinueCourseResponse course = courseService.getLatestContinueCourse(userId);
        if (course == null) {
            return ResponseEntity.noContent().build(); // 204
        }
        return ResponseEntity.ok(ApiResponse.success(ErrorCode.SUCCESS, course));
    }
    @GetMapping("/continue")
    public ResponseEntity<?> getContinueCourses(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @AuthenticationPrincipal User user
    ) {
        Long userId = user != null ? user.getId() : null;
        Page<ContinueCourseResponse> courses = courseService.getAllContinueCourses(userId, PageRequest.of(page, size));
        PagedResponse<ContinueCourseResponse> response = new PagedResponse<>(
                courses.getContent(), courses.getNumber(), courses.getSize(),
                courses.getTotalElements(), courses.getTotalPages(), courses.isLast()
        );
        return ResponseEntity.ok(ApiResponse.success(ErrorCode.SUCCESS, response));
    }
    @GetMapping("/completed")
    public ResponseEntity<?> getContinueCoursesComplete(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @AuthenticationPrincipal User user
    ) {
        Long userId = user != null ? user.getId() : null;
        Page<ContinueCourseResponse> courses = courseService.getAllContinueCoursesCompleted(userId, PageRequest.of(page, size));
        PagedResponse<ContinueCourseResponse> response = new PagedResponse<>(
                courses.getContent(), courses.getNumber(), courses.getSize(),
                courses.getTotalElements(), courses.getTotalPages(), courses.isLast()
        );
        return ResponseEntity.ok(ApiResponse.success(ErrorCode.SUCCESS, response));
    }

    @GetMapping("/search")
    public ResponseEntity<?> getCourseSearch(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size, @AuthenticationPrincipal User user){
        Long userId = user != null ? user.getId() : null;
        Page<CourseResponse> courses = courseService.getCourseSearch(userId,keyword, PageRequest.of(page, size));
        PagedResponse<CourseResponse> response = new PagedResponse<>(
                courses.getContent(), courses.getNumber(), courses.getSize(),
                courses.getTotalElements(), courses.getTotalPages(), courses.isLast()
        );
        return ResponseEntity.ok(ApiResponse.success(ErrorCode.SUCCESS, response));

    }
}
