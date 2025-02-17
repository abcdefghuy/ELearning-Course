package com.example.Learning_Course_App.service.Impl;

import com.example.Learning_Course_App.dto.response.CategoryResponse;
import com.example.Learning_Course_App.entity.Category;
import com.example.Learning_Course_App.mapper.ICategoryResponseMapper;
import com.example.Learning_Course_App.repository.ICategoryRepository;
import com.example.Learning_Course_App.service.ICategoryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements ICategoryService {
    private final ICategoryRepository categoryRepository;
    private final ICategoryResponseMapper categoryResponseMapper;
    public CategoryServiceImpl(ICategoryRepository categoryRepository, ICategoryResponseMapper categoryResponseMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryResponseMapper = categoryResponseMapper;
    }
    @Override
    public List<Category> findByCategoryNameContaining(String name) {
        return List.of();
    }

    @Override
    public Page<Category> findByCategoryNameContaining(String name, Pageable pageable) {
        return null;
    }

    @Override
    public Optional<Category> findByCategoryName(String name) {
        return Optional.empty();
    }

    @Override
    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(categoryResponseMapper::toDto)
                .collect(Collectors.toList());
    }
}
