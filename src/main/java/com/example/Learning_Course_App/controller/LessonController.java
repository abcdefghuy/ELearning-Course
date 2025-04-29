package com.example.Learning_Course_App.controller;

import com.example.Learning_Course_App.dto.response.ApiResponse;
import com.example.Learning_Course_App.dto.response.LessonResponse;
import com.example.Learning_Course_App.dto.response.PagedResponse;
import com.example.Learning_Course_App.entity.User;
import com.example.Learning_Course_App.enumeration.ErrorCode;
import com.example.Learning_Course_App.service.ILessonService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
        Page<LessonResponse> lessons = lessonService.getLessonsByCourse(courseId, userId, page, size);
        PagedResponse<LessonResponse> response = new PagedResponse<>(
                lessons.getContent(),
                lessons.getNumber(),
                lessons.getSize(),
                lessons.getTotalElements(),
                lessons.getTotalPages(),
                lessons.isLast()
        );
        return ResponseEntity.ok(ApiResponse.success(ErrorCode.SUCCESS, response));
    }
    @PostMapping("/update-progress/{lessonId}")
    public ResponseEntity<?> updateProgress(@PathVariable Long lessonId , @AuthenticationPrincipal User user) {
        try  {
            Long userId = user != null ? user.getId() : null;
            lessonService.updateProgress(lessonId, userId);
            return ResponseEntity.ok(ApiResponse.success(ErrorCode.LESSON_PROGRESS_UPDATED, null));
        }
        catch (Exception e) {
            return ResponseEntity.status(500).body(ApiResponse.error(ErrorCode.INTERNAL_SERVER_ERROR));
        }

    }

}
