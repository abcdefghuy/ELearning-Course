package com.example.Learning_Course_App.controller;

import com.example.Learning_Course_App.aop.ApiException;
import com.example.Learning_Course_App.dto.request.ReviewRequest;
import com.example.Learning_Course_App.dto.response.ApiResponse;
import com.example.Learning_Course_App.dto.response.PagedResponse;
import com.example.Learning_Course_App.dto.response.ReviewResponse;
import com.example.Learning_Course_App.entity.User;
import com.example.Learning_Course_App.enumeration.ErrorCode;
import com.example.Learning_Course_App.service.IReviewService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/reviews")
public class ReviewController {
    private final IReviewService reviewService;
    public ReviewController(IReviewService reviewService) {
        this.reviewService = reviewService;
    }
    @GetMapping("/course/{courseId}")
    public ResponseEntity<?> getReviewsByCourse(@PathVariable Long courseId,
                                                @RequestParam(defaultValue = "0") int page,
                                                @RequestParam(defaultValue = "10") int size) {
        // Assuming userId is not needed for this endpoint
        Pageable pageable = PageRequest.of(page, size);
        Page<ReviewResponse> reviews = reviewService.getReviewsByCourse(pageable, courseId);
        PagedResponse<ReviewResponse> response = new PagedResponse<>(
                reviews.getContent(),
                reviews.getNumber(),
                reviews.getSize(),
                reviews.getTotalElements(),
                reviews.getTotalPages(),
                reviews.isLast()
        );
        return ResponseEntity.ok(ApiResponse.success(ErrorCode.SUCCESS, response));
    }
    @PostMapping()
    public ResponseEntity<?> createReview(@RequestBody @Valid ReviewRequest reviewRequest, @AuthenticationPrincipal User user) {
        try {
            Long userId = user != null ? user.getId() : null;
            reviewRequest.setUserId(userId);
            reviewService.addReview(reviewRequest);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success(ErrorCode.REVIEW_ADDED_SUCCESSFULLY, null));
        } catch (ApiException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(ApiResponse.error(e.getErrorCode()));
        }
    }
}
