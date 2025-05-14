package com.example.Learning_Course_App.mapper;

import com.example.Learning_Course_App.dto.response.CategoryResponse;
import com.example.Learning_Course_App.entity.Category;
import org.springframework.stereotype.Service;

@Service
public class CategoryMapper {
    public CategoryResponse toDTO(Category category) {
        return CategoryResponse.builder()
                .categoryId(category.getCategoryId())
                .categoryName(category.getCategoryName())
                .categoryImageUrl(category.getCategoryImageUrl())
                .build();
    }
}
