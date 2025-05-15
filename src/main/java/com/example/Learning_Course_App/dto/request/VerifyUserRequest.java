package com.example.Learning_Course_App.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VerifyUserRequest {
    @Email(message = "INVALID_EMAIL_FORMAT")
    private String email;

    @Size(min = 6, max = 6, message = "INVALID_OTP_FORMAT")
    private String verificationCode;

    private String action;
}