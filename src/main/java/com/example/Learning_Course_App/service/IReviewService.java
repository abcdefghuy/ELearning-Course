package com.example.Learning_Course_App.service;

import com.example.Learning_Course_App.dto.request.ReviewRequest;
import com.example.Learning_Course_App.dto.response.ReviewResponse;
import com.example.Learning_Course_App.entity.Review;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

public interface IReviewService {
    public Page<ReviewResponse> getReviewsByCourse(Pageable pageable, Long course);

    void addReview(@Valid ReviewRequest reviewRequest);
    public void updateReview(Long reviewId, ReviewRequest reviewRequest);
    public void deleteReview(Long reviewId);
}
