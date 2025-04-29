package com.example.Learning_Course_App.service;

import com.example.Learning_Course_App.dto.request.UserRequest;
import com.example.Learning_Course_App.dto.response.CertificateResponse;
import com.example.Learning_Course_App.dto.response.UserDetailResponse;
import com.example.Learning_Course_App.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;

public interface IUserService {
    List<User> allUsers();
    UserDetailResponse getUserInfo(Long userId);

    void updateUserInfo(UserRequest user, Long userId);

    CertificateResponse getUserCertificates(Long userId, Long courseId);
}
