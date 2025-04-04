package com.example.Learning_Course_App.mapper;

import com.example.Learning_Course_App.dto.response.CategoryResponse;
import com.example.Learning_Course_App.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ICategoryResponseMapper {
    ICategoryResponseMapper INSTANCE =  Mappers.getMapper(ICategoryResponseMapper.class);
    @Mapping(source = "categoryId", target = "categoryId")
    CategoryResponse toDto(Category category);
}
