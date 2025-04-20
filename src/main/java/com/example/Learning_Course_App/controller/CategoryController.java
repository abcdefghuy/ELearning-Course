package com.example.Learning_Course_App.controller;

import com.example.Learning_Course_App.dto.response.CategoryResponse;
import com.example.Learning_Course_App.dto.response.CourseDetailResponse;
import com.example.Learning_Course_App.entity.Category;
import com.example.Learning_Course_App.entity.Course;
import com.example.Learning_Course_App.entity.User;
import com.example.Learning_Course_App.service.ICategoryService;
import com.example.Learning_Course_App.service.ICourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    private final ICategoryService categoryService;
    public CategoryController(ICategoryService categoryService) {
        this.categoryService = categoryService;
    }
    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getAllCategories(@AuthenticationPrincipal User person) {
        if (person == null) {
            System.out.println("User is not authenticated");
            return ResponseEntity.status(403).build(); // Forbidden
        }
        System.out.println("User is authenticated: " + person.getId());
        List<CategoryResponse> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }
}
