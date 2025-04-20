package com.example.Learning_Course_App.controller;

import com.example.Learning_Course_App.dto.response.LessonResponse;
import com.example.Learning_Course_App.dto.response.PagedResponse;
import com.example.Learning_Course_App.entity.User;
import com.example.Learning_Course_App.service.ILessonService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/lessons")
public class LessonController {
    private final ILessonService lessonService;
    public LessonController(ILessonService lessonService) {
        this.lessonService = lessonService;
    }
    @GetMapping("/{courseId}")
    public ResponseEntity<?> getLessonsByCourse(@PathVariable Long courseId,
                                                @RequestParam(defaultValue = "0") int page,
                                                @RequestParam(defaultValue = "10") int size, @AuthenticationPrincipal User user) {
        Long userId = user != null ? user.getId() : null;
        System.out.println(userId);
        Page<LessonResponse> lessons = lessonService.getLessonsByCourse(courseId, userId, page, size);
        PagedResponse<LessonResponse> response = new PagedResponse<>(
                lessons.getContent(),
                lessons.getNumber(),
                lessons.getSize(),
                lessons.getTotalElements(),
                lessons.getTotalPages(),
                lessons.isLast()
        );
        return ResponseEntity.ok(response);
    }

}
