package com.example.Learning_Course_App.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class QuizQuestion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long quizId;
    @Column(name="question_text",nullable = false, columnDefinition = "TEXT", length = 1000)
    private String questionText;
    @Column(name="option_a", columnDefinition = "TEXT", length = 1000)
    private String optionA;
    @Column(name="option_b", columnDefinition = "TEXT", length = 1000)
    private String optionB;
    @Column(name="option_c", columnDefinition = "TEXT", length = 1000)
    private String optionC;
    @Column(name="option_d", columnDefinition = "TEXT", length = 1000)
    private String optionD;
    @Column(name="correct_answer", length = 1000)
    private int correctAnswer;
    @ManyToOne
    @JoinColumn(name = "lesson_id", nullable = false)
    private Lesson lesson;
}
