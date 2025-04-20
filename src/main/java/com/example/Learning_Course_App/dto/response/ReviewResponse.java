package com.example.Learning_Course_App.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewResponse {
    private long reviewId;
    private long courseId;
    private String reviewUserName;
    private String reviewContent;
    private Date reviewDate;
    private int reviewScore;
    private String reviewerAvatarUrl;
}
