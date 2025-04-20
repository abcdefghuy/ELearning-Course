package com.example.Learning_Course_App.service.Impl;

import com.example.Learning_Course_App.dto.response.CategoryResponse;
import com.example.Learning_Course_App.entity.Category;
import com.example.Learning_Course_App.mapper.CategoryMapper;
import com.example.Learning_Course_App.repository.ICategoryRepository;
import com.example.Learning_Course_App.service.ICategoryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements ICategoryService {
    private final ICategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final RedisService redisService;
    public CategoryServiceImpl(ICategoryRepository categoryRepository, CategoryMapper categoryMapper, RedisService redisService) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
        this.redisService = redisService;
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
        redisService.delete("categories");
        String cacheKey = "categories";
        if(redisService.exists(cacheKey)) {
            List<CategoryResponse> cachedCategories = redisService.getList(cacheKey, CategoryResponse.class);
            if (cachedCategories != null) {
                return cachedCategories;
            }
        }
        List<CategoryResponse> categories = categoryRepository.findAll().stream()
                .map(categoryMapper::toDTO)
                .collect(Collectors.toList());

        redisService.saveList(cacheKey, categories, 60);
        return categories;
    }
}
