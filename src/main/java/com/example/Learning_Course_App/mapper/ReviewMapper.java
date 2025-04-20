package com.example.Learning_Course_App.mapper;

import com.example.Learning_Course_App.dto.request.ReviewRequest;
import com.example.Learning_Course_App.dto.response.ReviewResponse;
import com.example.Learning_Course_App.entity.Course;
import com.example.Learning_Course_App.entity.Review;
import com.example.Learning_Course_App.entity.User;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ReviewMapper {
    public ReviewResponse toDto(Review review) {
        return ReviewResponse.builder()
                .reviewContent(review.getFeedback())
                .reviewDate(review.getUpdate_at())
                .reviewUserName(review.getUser().getUsername())
                .reviewScore(review.getRating())
                .reviewId(review.getId())
                .courseId(review.getCourse().getId())
                .reviewerAvatarUrl(review.getUser().getAvatarUrl())
                .build();
    }

    public Review toEntity(ReviewRequest reviewRequest, User user, Course course) {
        Review review = new Review();
        review.setRating(reviewRequest.getRating());
        review.setFeedback(reviewRequest.getFeedback());
        review.setUpdate_at(reviewRequest.getUpdateAt());
        review.setUser(user);
        review.setCourse(course);
        return review;
    }
}
