package com.example.Learning_Course_App.service;

import com.example.Learning_Course_App.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;

public interface IUserService {
    List<User> allUsers();
}
