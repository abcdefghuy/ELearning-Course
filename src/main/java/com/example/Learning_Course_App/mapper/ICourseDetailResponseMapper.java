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
//    ICourseDetailResponseMapper INSTANCE =  Mappers.getMapper(ICourseDetailResponseMapper.class);
//    @Mapping(source = "course.courseDetail.coursePrice", target = "coursePrice")
//    @Mapping(source = "course.courseDetail.courseDescription", target = "courseDescription")
//    @Mapping(source = "course.courseDetail.duration", target = "duration")
//    @Mapping(source = "course.courseDetail.createdAt", target = "createdAt")
//    @Mapping(source = "course.courseDetail.rating", target = "rating")
//    @Mapping(target = "categoryNames", expression = "java(mapCategoryNames(course))")
//    @Mapping(source = "course.id",target = "courseId")
//    CourseDetailResponse toDto(Course course);
//
//    default List<String> mapCategoryNames(Course course) {
//        return course.getCategories() != null ?
//                course.getCategories().stream().map(c -> c.getCategory().getCategoryName()).collect(Collectors.toList()) : null;
//    }
}
