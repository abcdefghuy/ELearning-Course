package com.example.Learning_Course_App.controller;

import com.example.Learning_Course_App.dto.response.CategoryResponse;
import com.example.Learning_Course_App.dto.response.CourseDetailResponse;
import com.example.Learning_Course_App.entity.Category;
import com.example.Learning_Course_App.entity.Course;
import com.example.Learning_Course_App.service.ICategoryService;
import com.example.Learning_Course_App.service.ICourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/category")
public class CategoryController {
    private final ICategoryService categoryService;
    private final ICourseService courseService;
    public CategoryController(ICategoryService categoryService, ICourseService courseService) {
        this.categoryService = categoryService;
        this.courseService = courseService;
    }
    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getAllCategories() {
        List<CategoryResponse> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }
    @GetMapping("/{categoryId}/courses")
    public ResponseEntity<List<CourseDetailResponse>> getCourseByCategory(@PathVariable Long categoryId) {
        List<CourseDetailResponse> products = courseService.getCourseByCategory(categoryId);
        return ResponseEntity.ok(products);
    }
}
