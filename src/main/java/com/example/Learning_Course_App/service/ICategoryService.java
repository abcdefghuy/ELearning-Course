package com.example.Learning_Course_App.service;

import com.example.Learning_Course_App.dto.response.CategoryResponse;
import com.example.Learning_Course_App.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

public interface ICategoryService {
    List<Category> findByCategoryNameContaining(String name);
    //Tìm kiếm và Phân trang
    Page<Category> findByCategoryNameContaining(String name, Pageable pageable);
    Optional<Category> findByCategoryName(String name);
    List<CategoryResponse> getAllCategories();
}
