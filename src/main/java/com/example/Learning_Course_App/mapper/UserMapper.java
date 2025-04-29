package com.example.Learning_Course_App.mapper;

import com.example.Learning_Course_App.dto.request.UserRequest;
import com.example.Learning_Course_App.dto.response.UserDetailResponse;
import com.example.Learning_Course_App.entity.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Service
public class UserMapper {
    public UserDetailResponse toDTO(User user) {
        return UserDetailResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .phoneNumber(user.getPhoneNumber())
                .dateOfBirth(user.getDateOfBirth())
                .avatarUrl(user.getAvatarUrl())
                .gender(user.getGender())
                .build();
    }
}
