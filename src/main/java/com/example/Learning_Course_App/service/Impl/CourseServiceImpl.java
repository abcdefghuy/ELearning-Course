package com.example.Learning_Course_App.service.Impl;

import com.example.Learning_Course_App.dto.response.CourseDetailResponse;
import com.example.Learning_Course_App.mapper.ICourseDetailResponseMapper;
import com.example.Learning_Course_App.repository.ICourseRepository;
import com.example.Learning_Course_App.service.ICourseService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseServiceImpl implements ICourseService {
    private final ICourseRepository courseRepository;
    private final ICourseDetailResponseMapper courseDetailResponseMapper;
    public CourseServiceImpl(ICourseRepository courseRepository, ICourseDetailResponseMapper courseDetailResponseMapper) {
        this.courseRepository = courseRepository;
        this.courseDetailResponseMapper = courseDetailResponseMapper;
    }
    @Override
    public List<CourseDetailResponse> getCourseByCategory(Long categoryId) {
//        return courseRepository.findByCategoryId(categoryId).stream()
//                .map(courseDetailResponseMapper::toDto)
//                .collect(Collectors.toList());
        return null;
    }

    @Override
    public List<CourseDetailResponse> getTop10BestSellingProducts() {
//        Pageable pageable = PageRequest.of(0, 10);
//        return courseRepository.findTop10BySoldQuantity(pageable)
//                .stream()
//                .map(courseDetailResponseMapper::toDto)
//                .collect(Collectors.toList());
        return null;
    }

    @Override
    public List<CourseDetailResponse> getTop10NewProducts() {
//        LocalDate sevenDaysAgo = LocalDate.now().minusDays(7);
//        Pageable pageable = PageRequest.of(0, 10);
//        return courseRepository.findTop10ByCreatedAt(sevenDaysAgo, pageable)
//                .stream()
//                .map(courseDetailResponseMapper::toDto)
//                .collect(Collectors.toList());
        return null;
    }
}
