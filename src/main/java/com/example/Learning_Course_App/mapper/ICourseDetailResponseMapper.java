package com.example.Learning_Course_App.mapper;

import com.example.Learning_Course_App.dto.response.CategoryResponse;
import com.example.Learning_Course_App.dto.response.CourseDetailResponse;
import com.example.Learning_Course_App.entity.Category;
import com.example.Learning_Course_App.entity.Course;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface ICourseDetailResponseMapper {
    ICourseDetailResponseMapper INSTANCE =  Mappers.getMapper(ICourseDetailResponseMapper.class);
    @Mapping(source = "course.courseDetails.coursePrice", target = "coursePrice")
    @Mapping(source = "course.courseDetails.courseDescription", target = "courseDescription")
    @Mapping(source = "course.courseDetails.duration", target = "duration")
    @Mapping(source = "course.courseDetails.createdAt", target = "createdAt")
    @Mapping(source = "course.courseDetails.level", target = "level")
    @Mapping(source = "course.courseDetails.rating", target = "rating")
    @Mapping(source = "course.courseDetails.studentQuantity", target = "studentQuantity")
    @Mapping(target = "categoryNames", expression = "java(mapCategoryNames(course))")
    @Mapping(source = "course.id",target = "courseId")
    CourseDetailResponse toDto(Course course);

    default List<String> mapCategoryNames(Course course) {
        return course.getCategories() != null ?
                course.getCategories().stream().map(c -> c.getCategoryName()).collect(Collectors.toList()) : null;
    }
}
