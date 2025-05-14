package com.example.Learning_Course_App.service.Impl;

import com.example.Learning_Course_App.dto.response.ContinueCourseResponse;
import com.example.Learning_Course_App.dto.response.CourseDetailResponse;
import com.example.Learning_Course_App.dto.response.CourseResponse;
import com.example.Learning_Course_App.entity.Category;
import com.example.Learning_Course_App.entity.Course;
import com.example.Learning_Course_App.enumeration.LessonStatus;
import com.example.Learning_Course_App.enumeration.Status;
import com.example.Learning_Course_App.mapper.ContinueCourseMapper;
import com.example.Learning_Course_App.mapper.CourseDetailMapper;
import com.example.Learning_Course_App.mapper.CourseMapper;
import com.example.Learning_Course_App.repository.IBookmarkCourseRepository;
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
    private final IBookmarkCourseRepository bookmarkRepository;
    private final RedisService redisService;
    private final CourseMapper courseMapper;
    private final  CourseDetailMapper courseDetailResponseMapper;
    private final ContinueCourseMapper continueCourseMapper;
    private final ICategoryRepository categoryRepository;

    public CourseServiceImpl(ICourseRepository courseRepository, CourseDetailMapper courseDetailMapper, IBookmarkCourseRepository bookmarkRepository, RedisService redisService, CourseMapper courseMapper, CourseDetailMapper courseDetailResponseMapper, ContinueCourseMapper continueCourseMapper, ICategoryRepository categoryRepository) {
        this.courseRepository = courseRepository;
        this.courseDetailMapper = courseDetailMapper;
        this.bookmarkRepository = bookmarkRepository;
        this.redisService = redisService;
        this.courseMapper = courseMapper;
        this.courseDetailResponseMapper = courseDetailResponseMapper;
        this.continueCourseMapper = continueCourseMapper;
        this.categoryRepository = categoryRepository;
    }

    public Page<CourseResponse> getCourseByCategory(String categoryName, int page, int size, Long userId) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Course> listCourses = courseRepository.findByCategoryName(categoryName, pageable);

        // Chuyển đổi dữ liệu thành CourseResponse và đánh dấu bookmark
        List<CourseResponse> courseDTOs = ProcessBookmark(userId, listCourses);

        return new PageImpl<>(courseDTOs, pageable, listCourses.getTotalElements());
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

        List<CourseResponse> courseDTOs = ProcessBookmark(userId, topCourses);

        redisService.saveList("top10_course", courseDTOs, 10);

        // Trả về 1 Page thủ công
        return new PageImpl<>(courseDTOs, pageable, topCourses.getTotalElements());
    }
    @Override
    public Page<CourseResponse> getAllCourses(PageRequest of, Long userId) {
        // Kiểm tra cache
        List<CourseResponse> cachedCourses = redisService.getList("all_course", CourseResponse.class);
        if (cachedCourses != null && !cachedCourses.isEmpty()) {
            return paginateCourse(cachedCourses, of.getPageNumber(), of.getPageSize());
        }

        // Nếu không có dữ liệu trong cache, truy vấn cơ sở dữ liệu
        Page<Course> coursePage = courseRepository.findAll(of);
        List<CourseResponse> courseDTOs = ProcessBookmark(userId, coursePage);

        // Cập nhật cache sau khi truy vấn thành công
        redisService.saveList("all_course", courseDTOs, 10);

        return new PageImpl<>(courseDTOs, of, coursePage.getTotalElements());
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


    private List<Long> getBookmarkedCourseIds(Long userId, List<Long> courseIds) {
        return bookmarkRepository.findBookmarkedCourseIds(userId, courseIds);
    }

    public  CourseDetailResponse getDetailCourse(Long courseId, Long userId) {
        Course course = courseRepository.findById(courseId).orElse(null);
        if (course == null) {
            return null;
        }
        boolean isEnrolled = false;
        if (userId != null) {
            isEnrolled = course.getEnrollments()
                    .stream()
                    .anyMatch(e -> e.getUser().getId().equals(userId));
        }

        return courseDetailMapper.toDTO(course, isEnrolled);
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

    @Override
    public Page<CourseResponse> getCourseSearch(Long userId,String keyword, PageRequest of) {
        Page<Course> coursePages = courseRepository.findCourseByKeyWord(keyword ,of);
        List<CourseResponse> courseDTOs = ProcessBookmark(userId, coursePages);
        return new PageImpl<>(courseDTOs, of, coursePages.getTotalElements());
    }

    private List<CourseResponse> ProcessBookmark(Long userId, Page<Course> coursePages) {
        List<Long> ids = coursePages.getContent().stream()
                .map(Course::getId)
                .collect(Collectors.toList());

        List<Long> bookmarkedCourseId = getBookmarkedCourseIds(userId, ids);

        List<CourseResponse> courseDTOs = coursePages.getContent().stream()
                .map(course -> courseMapper.toDTO(course, bookmarkedCourseId.contains(course.getId())))
                .collect(Collectors.toList());
        return courseDTOs;
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
