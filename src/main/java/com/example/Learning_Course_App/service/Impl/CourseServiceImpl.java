package com.example.Learning_Course_App.service.Impl;

import com.example.Learning_Course_App.dto.response.CourseDetailResponse;
import com.example.Learning_Course_App.entity.Course;
import com.example.Learning_Course_App.mapper.CourseDetailMapper;
import com.example.Learning_Course_App.repository.IBookMarkRepository;
import com.example.Learning_Course_App.repository.ICourseRepository;
import com.example.Learning_Course_App.service.ICourseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseServiceImpl implements ICourseService {
    private final ICourseRepository courseRepository;
    private final CourseDetailMapper courseDetailMapper;
    private final IBookMarkRepository bookmarkRepository;
    private final RedisService redisService;
    public CourseServiceImpl(ICourseRepository courseRepository, CourseDetailMapper courseDetailMapper, IBookMarkRepository bookmarkRepository, RedisService redisService) {
        this.courseRepository = courseRepository;
        this.courseDetailMapper = courseDetailMapper;
        this.bookmarkRepository = bookmarkRepository;
        this.redisService = redisService;
    }
    @Override
    public List<CourseDetailResponse> getCourseByCategory(Long categoryId) {
//        return courseRepository.findByCategoryId(categoryId).stream()
//                .map(courseDetailResponseMapper::toDto)
//                .collect(Collectors.toList());
        return null;
    }

    @Override
    public Page<CourseDetailResponse> getTop10BestSellingProducts(Long userId) {
        redisService.delete("top10_course");

        // kiểm tra cache
        List<CourseDetailResponse> cachedCourses = redisService.getList("top10_course", CourseDetailResponse.class);
        if (cachedCourses != null && !cachedCourses.isEmpty()) {
            return paginateCourse(cachedCourses,1, 10); // nếu cached toàn bộ thì dùng hàm này
        }

        Pageable pageable = PageRequest.of(0, 10);
        Page<Course> topCourses = courseRepository.findTop10BestSellers(pageable);

        // map và đánh dấu bookmark
        List<CourseDetailResponse> courseDTOs = topCourses.getContent().stream()
                .map(courseDetailMapper::toDTO)
                .collect(Collectors.toList());
        List<CourseDetailResponse> dto = markBookmarked(courseDTOs, userId);

        redisService.saveList("top10_course", dto, 10);

        // Trả về 1 Page thủ công
        return new PageImpl<>(dto, pageable, topCourses.getTotalElements());
    }

    private List<CourseDetailResponse> markBookmarked(List<CourseDetailResponse> courses, Long userId) {
        if (userId == null) {
            // Guest thì không cần đánh dấu bookmarked
            for (CourseDetailResponse dto : courses) {
                dto.setBookmarked(false); // hoặc không set cũng được
            }
            return courses;
        }

        List<Long> courseIds = courses.stream()
                .map(CourseDetailResponse::getCourseId)
                .collect(Collectors.toList());

        List<Long> bookmarkedCourseIds = bookmarkRepository
                .findBookmarkedCourseIdsByUserIdAndCourseIds(userId, courseIds);

        for (CourseDetailResponse dto : courses) {
            dto.setBookmarked(bookmarkedCourseIds.contains(dto.getCourseId()));
        }

        return courses;
    }

    private Page<CourseDetailResponse> paginateCourse(List<CourseDetailResponse> cachedCourses, int page, int size) {
        int start = page  * size;
        int end = Math.min(start + size, cachedCourses.size());
        return new PageImpl<>(cachedCourses.subList(start, end), PageRequest.of(page , size), cachedCourses.size());
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

    @Override
    public Page<CourseDetailResponse> getAllCourses(PageRequest of, Long userId) {
        // kiểm tra cache
        List<CourseDetailResponse> cachedCourses = redisService.getList("all_course", CourseDetailResponse.class);
        if (cachedCourses != null && !cachedCourses.isEmpty()) {
            return paginateCourse(cachedCourses, of.getPageNumber() , of.getPageSize());
        }
        Page<Course> coursePage = courseRepository.findAll(of);
        List<CourseDetailResponse> courseDTOs = coursePage.getContent().stream()
                .map(courseDetailMapper::toDTO)
                .collect(Collectors.toList());
        List<CourseDetailResponse> dto = markBookmarked(courseDTOs, userId);
        redisService.saveList("all_course", dto, 10);
        return new PageImpl<>(dto, of, coursePage.getTotalElements());
    }
}
