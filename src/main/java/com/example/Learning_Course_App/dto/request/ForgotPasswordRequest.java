package com.example.Learning_Course_App.dto.request;

import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ForgotPasswordRequest {
    @Email(message = "INVALID_EMAIL_FORMAT")
    private String email;
}
