package com.example.Learning_Course_App.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterUserRequest {
    private String email;
    private String password;
    private String username;
}