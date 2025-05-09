package com.example.Learning_Course_App.dto.response;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class QuizQuestionResponse {
    private Long id;
    private String questionText;
    private List<String> options;
    private int correctIndex;
    private Long lessonId;
}
