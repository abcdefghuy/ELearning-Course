package com.example.Learning_Course_App.controller;

import com.example.Learning_Course_App.aop.ApiException;
import com.example.Learning_Course_App.dto.request.BookmarkRequest;
import com.example.Learning_Course_App.dto.response.ApiResponse;
import com.example.Learning_Course_App.dto.response.CourseResponse;
import com.example.Learning_Course_App.dto.response.PagedResponse;
import com.example.Learning_Course_App.entity.User;
import com.example.Learning_Course_App.enumeration.ErrorCode;
import com.example.Learning_Course_App.service.IBookmarkService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/bookmarks")
public class BookmarkController {

    private final IBookmarkService bookmarkService;

    public BookmarkController(IBookmarkService bookmarkService) {
        this.bookmarkService = bookmarkService;
    }

    // GET bookmarks by userId
    @GetMapping("/user")
    public ResponseEntity<ApiResponse<PagedResponse<CourseResponse>>> getBookmarksByUserId(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size, @AuthenticationPrincipal User user) {
        Long userId = user.getId();
        Pageable pageable = Pageable.ofSize(size).withPage(page);
        Page<CourseResponse> courses = bookmarkService.getBookmarksByUserId(userId, pageable);
        PagedResponse<CourseResponse> response = new PagedResponse<>(
                courses.getContent(),
                courses.getNumber(),
                courses.getSize(),
                courses.getTotalElements(),
                courses.getTotalPages(),
                courses.isLast()
        );
        return ResponseEntity.ok(ApiResponse.success(ErrorCode.SUCCESS,response));
    }

    // POST: Create new bookmark
    @PostMapping
    public ResponseEntity<ApiResponse<?>> addBookmark(@RequestBody @Valid BookmarkRequest request, @AuthenticationPrincipal User user) {
        try {
            bookmarkService.addBookmark(request, user.getId());
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success(ErrorCode.BOOKMARK_ADDED_SUCCESSFULLY, null));
        } catch (ApiException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(ApiResponse.error(e.getErrorCode()));
        }
    }

    // DELETE: Remove bookmark
    @DeleteMapping("/{courseId}")
    public ResponseEntity<?> removeBookmark(
            @PathVariable Long courseId,
            @AuthenticationPrincipal User user) {
        try {
            bookmarkService.removeBookmark(user.getId(), courseId);
            return ResponseEntity.ok(ApiResponse.success(ErrorCode.DELETED, null));
        } catch (ApiException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getErrorCode()));
        }
    }
}
