package com.example.Learning_Course_App.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LessonResponse {
    private long lessonId;
    private long courseId;
    private String lessonName;
    private String lessonDescription;
    private String status;
    private int duration;
    private String lessonVideoUrl;
    private int lessonOrder;
    private boolean isHasQuiz;
}
