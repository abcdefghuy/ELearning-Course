package com.example.Learning_Course_App.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResetPasswordRequest {
    @Email(message = "INVALID_EMAIL_FORMAT")
    private String email;

    @Size(min = 8, max = 30, message = "PASSWORD_LENGTH_INVALID")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,30}$",
            message = "PASSWORD_INVALID_FORMAT")
    private String newPassword;
}
