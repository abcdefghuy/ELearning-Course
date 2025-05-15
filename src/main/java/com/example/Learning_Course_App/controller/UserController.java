package com.example.Learning_Course_App.controller;

import com.example.Learning_Course_App.dto.request.UserRequest;
import com.example.Learning_Course_App.dto.response.ApiResponse;
import com.example.Learning_Course_App.dto.response.UserDetailResponse;
import com.example.Learning_Course_App.entity.User;
import com.example.Learning_Course_App.enumeration.ErrorCode;
import com.example.Learning_Course_App.service.IUserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final IUserService userService;

    public UserController(IUserService userService) {
        this.userService = userService;
    }
    @PutMapping("/update")
    public ResponseEntity<ApiResponse<Void>> updateUserInfo(@RequestBody UserRequest userRequest, @AuthenticationPrincipal User user) {
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error(ErrorCode.USER_NOT_FOUND));
        }
        try {
            Long userId = user.getId();
            userService.updateUserInfo(userRequest, userId);
            return ResponseEntity.ok(ApiResponse.success(ErrorCode.SUCCESS));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(ErrorCode.INTERNAL_SERVER_ERROR));
        }
    }

    @GetMapping("/me")
    public ResponseEntity<?> getUserInfo(@AuthenticationPrincipal User user) {
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error(ErrorCode.USER_NOT_FOUND));
        }
        UserDetailResponse userResponse = userService.getUserInfo(user.getId());
        return ResponseEntity.ok(ApiResponse.success(ErrorCode.SUCCESS, userResponse));
    }

    @GetMapping("/me/certificates")
    public ResponseEntity<?> getUserCertificates(@AuthenticationPrincipal User user, @RequestParam(name = "courseId",defaultValue ="0") Long courseId) {
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error(ErrorCode.USER_NOT_FOUND));
        }
        try {
            Long userId = user.getId();
            return ResponseEntity.ok(ApiResponse.success(ErrorCode.SUCCESS, userService.getUserCertificates(userId, courseId)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(ErrorCode.INTERNAL_SERVER_ERROR, e.getMessage()));
        }
    }
}
