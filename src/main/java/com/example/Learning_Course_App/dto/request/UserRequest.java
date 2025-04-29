package com.example.Learning_Course_App.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {
    @Email(message = "INVALID_EMAIL_FORMAT")
    private String email;
    private String fullName;
    private String phone;
    private String gender;
    private String birthday;
    private String avatarUrl;
}
