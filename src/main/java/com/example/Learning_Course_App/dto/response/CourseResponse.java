package com.example.Learning_Course_App.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CourseResponse {
    private long courseId;
    private String courseName;
    private boolean enabled;
    private Double coursePrice;
    private String courseImg;
    private boolean isBookmarked;
    private Double rating;
    private boolean isBestSeller;
    private String mentorName;
    private String mentorAvatar;
}
