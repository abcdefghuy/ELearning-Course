package com.example.Learning_Course_App.service.Impl;

import com.example.Learning_Course_App.aop.ApiException;
import com.example.Learning_Course_App.dto.request.ReviewRequest;
import com.example.Learning_Course_App.dto.response.ReviewResponse;
import com.example.Learning_Course_App.entity.Bookmark;
import com.example.Learning_Course_App.entity.Course;
import com.example.Learning_Course_App.entity.Review;
import com.example.Learning_Course_App.entity.User;
import com.example.Learning_Course_App.enumeration.ErrorCode;
import com.example.Learning_Course_App.mapper.ReviewMapper;
import com.example.Learning_Course_App.repository.ICourseRepository;
import com.example.Learning_Course_App.repository.IReviewRepository;
import com.example.Learning_Course_App.repository.IUserRepository;
import com.example.Learning_Course_App.service.IReviewService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ReviewServiceImpl implements IReviewService {
    private final ReviewMapper reviewMapper;
    private final IReviewRepository reviewRepository;
    private final IUserRepository userRepository;
    private final ICourseRepository courseRepository;
    public ReviewServiceImpl(ReviewMapper reviewMapper, IReviewRepository reviewRepository, IUserRepository userRepository, ICourseRepository courseRepository) {
        this.reviewMapper = reviewMapper;
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
    }

    @Override
    public Page<ReviewResponse> getReviewsByCourse(Pageable pageable, Long courseId) {
        Page<Review> reviews = reviewRepository.getReviewsByCourse(pageable,courseId);
        List<ReviewResponse> reviewResponses = reviews.getContent().stream()
                .map(reviewMapper::toDto)
                .toList();
        return new PageImpl<>(reviewResponses, pageable, reviews.getTotalElements());
    }

    @Override
    public void addReview(ReviewRequest reviewRequest) {
        User user = userRepository.findById(reviewRequest.getUserId())
                .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));

        Course course = courseRepository.findById(reviewRequest.getCourseId())
                .orElseThrow(() -> new ApiException(ErrorCode.COURSE_NOT_FOUND));

        Review review = reviewMapper.toEntity(reviewRequest, user, course);
        reviewRepository.save(review);
    }
    @Override
    public void deleteReview(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ApiException(ErrorCode.REVIEW_NOT_FOUND));
        reviewRepository.delete(review);
    }
    @Override
    public void updateReview(Long reviewId, ReviewRequest reviewRequest) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ApiException(ErrorCode.REVIEW_NOT_FOUND));
        review.setRating(reviewRequest.getRating());
        review.setFeedback(reviewRequest.getFeedback());
        review.setUpdate_at(reviewRequest.getUpdateAt());
        reviewRepository.save(review);
    }
}
