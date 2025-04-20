package com.example.Learning_Course_App.service;

import com.example.Learning_Course_App.dto.request.BookmarkRequest;
import com.example.Learning_Course_App.dto.response.CourseResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IBookmarkService {
    Page<CourseResponse> getBookmarksByUserId(Long userId, Pageable pageable);

    void addBookmark(@Valid BookmarkRequest request);

    void deleteBookmark(Long bookmarkId);
}
