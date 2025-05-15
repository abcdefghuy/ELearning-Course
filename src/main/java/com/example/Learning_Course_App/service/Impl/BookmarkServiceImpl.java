package com.example.Learning_Course_App.service.Impl;

import com.example.Learning_Course_App.aop.ApiException;
import com.example.Learning_Course_App.dto.request.BookmarkRequest;
import com.example.Learning_Course_App.dto.response.CourseResponse;
import com.example.Learning_Course_App.entity.Bookmark;
import com.example.Learning_Course_App.entity.BookmarkedCourse;
import com.example.Learning_Course_App.entity.Course;
import com.example.Learning_Course_App.entity.User;
import com.example.Learning_Course_App.enumeration.ErrorCode;
import com.example.Learning_Course_App.mapper.CourseMapper;

import com.example.Learning_Course_App.repository.IBookmarkCourseRepository;
import com.example.Learning_Course_App.repository.IBookmarkRepository;
import com.example.Learning_Course_App.repository.ICourseRepository;
import com.example.Learning_Course_App.repository.IUserRepository;
import com.example.Learning_Course_App.service.IBookmarkService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookmarkServiceImpl implements IBookmarkService {
    private final IBookmarkCourseRepository bookmarkRepository;
    private final CourseMapper courseMapper;
    private final IUserRepository userRepository;
    private final ICourseRepository courseRepository;
    private  final IBookmarkRepository bookmarkedRepository;
    private final RedisService redisService;
    public BookmarkServiceImpl(IBookmarkCourseRepository bookmarkRepository, CourseMapper courseMapper, IUserRepository userRepository, ICourseRepository courseRepository, IBookmarkRepository bookmarkedRepository, RedisService redisService) {
        this.bookmarkRepository = bookmarkRepository;
        this.courseMapper = courseMapper;
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
        this.bookmarkedRepository = bookmarkedRepository;
        this.redisService = redisService;
    }

    @Override
    public Page<CourseResponse> getBookmarksByUserId(Long userId, Pageable pageable) {
        String redisKey = "bookmarked_courses_" + userId;
        // 1. Check Redis cache
        List<CourseResponse> cachedCourses = redisService.getList(redisKey, CourseResponse.class);
        if (cachedCourses != null && !cachedCourses.isEmpty()) {
            return new PageImpl<>(cachedCourses, pageable, cachedCourses.size()); // or total count if known
        }
        // 2. Fetch from DB if not in Redis
        Page<Course> bookmarkedCourses = bookmarkRepository.findCoursesBookmarkedByUserId(userId, pageable);
        // 3. Map to CourseResponse (with isBookmarked = true)
        List<CourseResponse> responseList = bookmarkedCourses
                .getContent()
                .stream()
                .map(course -> courseMapper.toDTO(course, true))
                .collect(Collectors.toList());

        // 4. Save to Redis
        redisService.saveList(redisKey, responseList, 60 * 60); // Cache for 1 hour
        // 5. Return result
        return new PageImpl<>(responseList, pageable, bookmarkedCourses.getTotalElements());
    }

    @Override
    public void addBookmark(BookmarkRequest request,Long userId) {
        // Tìm người dùng theo userId
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));
        if (user.getBookmarks() == null) {
            Bookmark bookmark = new Bookmark();
            bookmark.setUser(user);
            bookmark.setCreatedAt(LocalDate.now());
            bookmarkedRepository.save(bookmark);
        }
        // Tìm khóa học theo courseId
        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new ApiException(ErrorCode.COURSE_NOT_FOUND));

        // Kiểm tra xem người dùng đã bookmark khóa học này chưa
        boolean exists = bookmarkRepository.existsByUserIdAndCourseId(user.getId(), course.getId());
        if (exists) {
            throw new ApiException(ErrorCode.BOOKMARK_ALREADY_EXISTS);
        }
        // Tạo và lưu bản ghi vào bảng BookmarkedCourse
        BookmarkedCourse bookmarkedCourse = new BookmarkedCourse();
        bookmarkedCourse.setCourse(course);
        bookmarkedCourse.setBookmark(user.getBookmarks());  // Lưu liên kết tới Bookmark của người dùng

        bookmarkRepository.save(bookmarkedCourse); // Lưu vào bảng BookmarkedCourse
        redisService.delete("all_course"); // Xóa cache để cập nhật lại danh sách khóa học
        redisService.delete("bookmarked_courses_" + userId); // Xóa cache cho trang đầu tiên
    }

    @Override
    public void removeBookmark(Long userId, Long courseId) {
        // Tìm bookmark theo bookmarkId
        BookmarkedCourse bookmarkedCourse = bookmarkRepository
                .findByUserIdAndCourseId(userId, courseId)
                .orElseThrow(() -> new ApiException(ErrorCode.BOOKMARK_NOT_FOUND));

        bookmarkRepository.delete(bookmarkedCourse);
        redisService.delete("all_course");
        redisService.delete("bookmarked_courses_" + userId); // Xóa cache cho trang đầu tiên
    }
    // Implement the methods for bookmark service here
    // For example, you can implement methods to add, remove, and retrieve bookmarks
}
