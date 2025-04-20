package com.example.Learning_Course_App.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ContinueCourseResponse {
    private Long courseId;
    private String courseTitle;
    private String categoryName;
    private String courseImageUrl;
    private int progress;
}
