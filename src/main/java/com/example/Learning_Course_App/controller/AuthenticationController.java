package com.example.Learning_Course_App.controller;


import com.example.Learning_Course_App.aop.ApiException;
import com.example.Learning_Course_App.dto.request.*;
import com.example.Learning_Course_App.dto.response.ApiResponse;
import com.example.Learning_Course_App.dto.response.LoginResponse;
import com.example.Learning_Course_App.enumeration.ErrorCode;
import com.example.Learning_Course_App.service.Impl.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/auth")
@RestController
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<Void>> register(@RequestBody @Valid RegisterUserRequest registerUserRequest) {
        authenticationService.signup(registerUserRequest);
        return ResponseEntity.ok(ApiResponse.success(ErrorCode.SUCCESS_OTP_SENT));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<?>> authenticate(@RequestBody @Valid LoginUserRequest loginUserRequest) {
        try {
            LoginResponse loginResponse = authenticationService.authenticate(loginUserRequest);
            return ResponseEntity.ok(ApiResponse.success(ErrorCode.LOGIN_SUCCESS, loginResponse));
        } catch (ApiException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error(e.getErrorCode()));
        }
    }

    @PostMapping("/verify")
    public ResponseEntity<ApiResponse<Void>> verifyUser(@RequestBody @Valid VerifyUserRequest verifyUserRequest) {
        authenticationService.verifyUser(verifyUserRequest);
        return ResponseEntity.ok(ApiResponse.success(ErrorCode.SUCCESS_ACCOUNT_VERIFIED));
    }

    @PostMapping("/resend")
    public ResponseEntity<ApiResponse<Void>> resendVerificationCode(@RequestParam @Valid String email) {
        authenticationService.resendVerificationCode(email);
        return ResponseEntity.ok(ApiResponse.success(ErrorCode.SUCCESS_OTP_SENT));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse<Void>> forgotPassword(@RequestBody @Valid ForgotPasswordRequest forgotPasswordRequest) {
        authenticationService.forgotPassword(forgotPasswordRequest);
        return ResponseEntity.ok(ApiResponse.success(ErrorCode.SUCCESS_OTP_SENT));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse<Void>> resetPassword(@RequestBody @Valid ResetPasswordRequest resetPasswordRequest) {
        authenticationService.resetPassword(resetPasswordRequest);
        return ResponseEntity.ok(ApiResponse.success(ErrorCode.SUCCESS_PASSWORD_RESET));
    }
}