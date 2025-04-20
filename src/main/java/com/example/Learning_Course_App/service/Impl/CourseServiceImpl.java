package com.example.Learning_Course_App.service.Impl;

import com.example.Learning_Course_App.dto.response.ContinueCourseResponse;
import com.example.Learning_Course_App.dto.response.CourseDetailResponse;
import com.example.Learning_Course_App.dto.response.CourseResponse;
import com.example.Learning_Course_App.dto.response.LessonResponse;
import com.example.Learning_Course_App.entity.Category;
import com.example.Learning_Course_App.entity.Course;
import com.example.Learning_Course_App.enumeration.LessonStatus;
import com.example.Learning_Course_App.enumeration.Status;
import com.example.Learning_Course_App.mapper.ContinueCourseMapper;
import com.example.Learning_Course_App.mapper.CourseDetailMapper;
import com.example.Learning_Course_App.mapper.CourseMapper;
import com.example.Learning_Course_App.mapper.LessonMapper;
import com.example.Learning_Course_App.repository.IBookMarkRepository;
import com.example.Learning_Course_App.repository.ICategoryRepository;
import com.example.Learning_Course_App.repository.ICourseRepository;
import com.example.Learning_Course_App.service.ICourseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseServiceImpl implements ICourseService {
    private final ICourseRepository courseRepository;
    private final CourseDetailMapper courseDetailMapper;
    private final IBookMarkRepository bookmarkRepository;
    private final RedisService redisService;
    private final CourseMapper courseMapper;
    private final  CourseDetailMapper courseDetailResponseMapper;
    private final ContinueCourseMapper continueCourseMapper;
    private final ICategoryRepository categoryRepository;

    public CourseServiceImpl(ICourseRepository courseRepository, CourseDetailMapper courseDetailMapper, IBookMarkRepository bookmarkRepository, RedisService redisService, CourseMapper courseMapper, CourseDetailMapper courseDetailResponseMapper, ContinueCourseMapper continueCourseMapper, ICategoryRepository categoryRepository) {
        this.courseRepository = courseRepository;
        this.courseDetailMapper = courseDetailMapper;
        this.bookmarkRepository = bookmarkRepository;
        this.redisService = redisService;
        this.courseMapper = courseMapper;
        this.courseDetailResponseMapper = courseDetailResponseMapper;
        this.continueCourseMapper = continueCourseMapper;
        this.categoryRepository = categoryRepository;
    }
    @Override
    public Page<CourseResponse> getCourseByCategory(Long categoryId, int page, int size, Long userId) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Course> listCourses = courseRepository.findByCategoryId(categoryId,pageable);
        List<CourseResponse> coursesDTO = listCourses.getContent().stream()
                .map(courseMapper::toDTO)
                .toList();
        List<CourseResponse> dto = markBookmarked(coursesDTO, userId);
        return new PageImpl<>(dto, pageable, listCourses.getTotalElements());
    }

    @Override
    public Page<CourseResponse> getTop10BestSellingProducts(Long userId) {
        redisService.delete("top10_course");
        // kiểm tra cache
        List<CourseResponse> cachedCourses = redisService.getList("top10_course", CourseResponse.class);
        if (cachedCourses != null && !cachedCourses.isEmpty()) {
            return paginateCourse(cachedCourses,0, 10); // nếu cached toàn bộ thì dùng hàm này
        }

        Pageable pageable = PageRequest.of(0, 10);
        Page<Course> topCourses = courseRepository.findTop10BestSellers(pageable);

        // map và đánh dấu bookmark
        List<CourseResponse> courseDTOs = topCourses.getContent().stream()
                .map(courseMapper::toDTO)
                .collect(Collectors.toList());
        List<CourseResponse> dto = markBookmarked(courseDTOs, userId);

        redisService.saveList("top10_course", dto, 10);

        // Trả về 1 Page thủ công
        return new PageImpl<>(dto, pageable, topCourses.getTotalElements());
    }

    private List<CourseResponse> markBookmarked(List<CourseResponse> courses, Long userId) {
        if (userId == null) {
            // Guest thì không cần đánh dấu bookmarked
            for (CourseResponse dto : courses) {
                dto.setBookmarked(false); // hoặc không set cũng được
            }
            return courses;
        }

        List<Long> courseIds = courses.stream()
                .map(CourseResponse::getCourseId)
                .collect(Collectors.toList());

        List<Long> bookmarkedCourseIds = bookmarkRepository
                .findBookmarkedCourseIdsByUserIdAndCourseIds(userId, courseIds);

        for (CourseResponse dto : courses) {
            dto.setBookmarked(bookmarkedCourseIds.contains(dto.getCourseId()));
        }

        return courses;
    }

    private Page<CourseResponse> paginateCourse(List<CourseResponse> cachedCourses, int page, int size) {
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
    public Page<CourseResponse> getAllCourses(PageRequest of, Long userId) {
        redisService.delete("all_course");
        // kiểm tra cache
        List<CourseResponse> cachedCourses = redisService.getList("all_course", CourseResponse.class);
        if (cachedCourses != null && !cachedCourses.isEmpty()) {
            return paginateCourse(cachedCourses, of.getPageNumber() , of.getPageSize());
        }
        Page<Course> coursePage = courseRepository.findAll(of);
        List<CourseResponse> courseDTOs = coursePage.getContent().stream()
                .map(courseMapper::toDTO)
                .collect(Collectors.toList());
        List<CourseResponse> dto = markBookmarked(courseDTOs, userId);
        redisService.saveList("all_course", dto, 10);
        return new PageImpl<>(dto, of, coursePage.getTotalElements());
    }

    public  CourseDetailResponse getDetailCourse(Long courseId, Long userId) {
        Course course = courseRepository.findById(courseId).orElse(null);
        if (course == null) {
            return null;
        }
        return courseDetailMapper.toDTO(course);
    }

    @Override
    public ContinueCourseResponse getLatestContinueCourse(Long userId) {
        Course latestCourse = courseRepository.findLastContinueCourse(
                userId, Status.IN_PROGRESS, LessonStatus.UNLOCKED
        );
        if (latestCourse == null) {
            return null;
        }
        int progressPercent = getCourseProgress(userId, latestCourse.getId(), Status.IN_PROGRESS);
        String categoryNames = getCourseCategoryNames(latestCourse.getId());
        return continueCourseMapper.toDTO(latestCourse, categoryNames, progressPercent);
    }

    @Override
    public Page<ContinueCourseResponse> getAllContinueCourses(Long userId, PageRequest of) {
        redisService.delete("continue_course_user:" + userId);
        List<ContinueCourseResponse> cachedCourses = redisService.getList("continue_course_user:"+ userId, ContinueCourseResponse.class);
        if (cachedCourses != null && !cachedCourses.isEmpty()) {
            return paginateContinueCourse(cachedCourses, of.getPageNumber(), of.getPageSize());
        }
        Page<Course> coursePage = courseRepository.findContinueCoursesCommon(userId,Status.IN_PROGRESS, LessonStatus.UNLOCKED, of);
        List<ContinueCourseResponse> courseDTOs = coursePage.getContent().stream()
                .map(course -> {
                    int progressPercent = getCourseProgress(userId, course.getId(), Status.IN_PROGRESS);
                    String categoryNames = getCourseCategoryNames(course.getId());
                    return continueCourseMapper.toDTO(course, categoryNames, progressPercent);
                })
                .toList();
        redisService.saveList("continue_course_user:" + userId, courseDTOs, 10);
        return new PageImpl<>(courseDTOs, of, coursePage.getTotalElements());
    }

    @Override
    public Page<ContinueCourseResponse> getAllContinueCoursesCompleted(Long userId, PageRequest of) {
        redisService.delete("completed_course_user:" + userId);
        List<ContinueCourseResponse> cachedCourses = redisService.getList("completed_course_user:" + userId, ContinueCourseResponse.class);
        if (cachedCourses != null && !cachedCourses.isEmpty()) {
           return paginateContinueCourse(cachedCourses, of.getPageNumber(), of.getPageSize());
        }
        Page<Course> coursePage = courseRepository.findAllCompletedCourses(userId,Status.COMPLETED, of);
        List<ContinueCourseResponse> courseDTOs = coursePage.getContent().stream()
                .map(course -> {
                    int progressPercent = getCourseProgress(userId, course.getId(), Status.COMPLETED);
                    String categoryNames = getCourseCategoryNames(course.getId());
                    return continueCourseMapper.toDTO(course, categoryNames, progressPercent);
                })
                .toList();
        redisService.saveList("completed_course_user:"+ userId, courseDTOs, 10);
        return new PageImpl<>(courseDTOs, of, coursePage.getTotalElements());
    }

    private int getCourseProgress(Long userId, Long courseId, Status status) {
        if (status == Status.COMPLETED) {
            return 100;
        }
        return courseRepository.getCourseProgressPercent(
                courseId, userId, LessonStatus.UNLOCKED, status
        );
    }
    private Page<ContinueCourseResponse> paginateContinueCourse(List<ContinueCourseResponse> cachedCourses, int page, int size) {
        int start = page  * size;
        int end = Math.min(start + size, cachedCourses.size());
        return new PageImpl<>(cachedCourses.subList(start, end), PageRequest.of(page , size), cachedCourses.size());
    }

    private String getCourseCategoryNames(Long courseId) {
        return categoryRepository.findCategoriesByCourseId(courseId).stream()
                .map(Category::getCategoryName)
                .collect(Collectors.joining(", "));
    }
}
