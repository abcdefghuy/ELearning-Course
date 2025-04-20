package com.example.Learning_Course_App.service.Impl;

import com.example.Learning_Course_App.aop.ApiException;
import com.example.Learning_Course_App.dto.request.BookmarkRequest;
import com.example.Learning_Course_App.dto.response.CourseResponse;
import com.example.Learning_Course_App.entity.Bookmark;
import com.example.Learning_Course_App.entity.Course;
import com.example.Learning_Course_App.entity.User;
import com.example.Learning_Course_App.enumeration.ErrorCode;
import com.example.Learning_Course_App.mapper.CourseMapper;
import com.example.Learning_Course_App.repository.IBookMarkRepository;
import com.example.Learning_Course_App.repository.ICourseRepository;
import com.example.Learning_Course_App.repository.IUserRepository;
import com.example.Learning_Course_App.service.IBookmarkService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookmarkServiceImpl implements IBookmarkService {
    private final IBookMarkRepository bookmarkRepository;
    private final CourseMapper courseMapper;
    private final IUserRepository userRepository;
    private final ICourseRepository courseRepository;

    public BookmarkServiceImpl(IBookMarkRepository bookmarkRepository, CourseMapper courseMapper, IUserRepository userRepository, ICourseRepository courseRepository) {
        this.bookmarkRepository = bookmarkRepository;
        this.courseMapper = courseMapper;
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
    }

    @Override
    public Page<CourseResponse> getBookmarksByUserId(Long userId, Pageable pageable) {
        Page<Course> bookmarkedCourses = bookmarkRepository.findCoursesBookmarkedByUser(userId, pageable);
        return bookmarkedCourses.map(courseMapper::toDTO);
    }

    @Override
    public void addBookmark(BookmarkRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));

        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new ApiException(ErrorCode.COURSE_NOT_FOUND));

        boolean exists = bookmarkRepository.existsByUserAndCourse(user.getId(), course.getId());
        if (exists) {
            throw new ApiException(ErrorCode.BOOKMARK_ALREADY_EXISTS);
        }
        Bookmark bookmark = new Bookmark();
        bookmark.setUser(user);
        bookmark.setCourse(course);
        bookmarkRepository.save(bookmark);
    }

    @Override
    public void deleteBookmark(Long bookmarkId) {
        // Tìm Bookmark theo bookmarkId
        Bookmark bookmark = bookmarkRepository.findById(bookmarkId)
                .orElseThrow(() -> new ApiException(ErrorCode.BOOKMARK_NOT_FOUND)); // Nếu không tìm thấy, ném lỗi

        // Xóa Bookmark
        bookmarkRepository.delete(bookmark);
    }
    // Implement the methods for bookmark service here
    // For example, you can implement methods to add, remove, and retrieve bookmarks
}
