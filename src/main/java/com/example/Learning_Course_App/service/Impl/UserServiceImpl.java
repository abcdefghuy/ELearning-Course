package com.example.Learning_Course_App.service.Impl;


import com.example.Learning_Course_App.entity.User;
import com.example.Learning_Course_App.repository.IUserRepository;
import com.example.Learning_Course_App.service.IUserService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements IUserService {
    private final IUserRepository IUserRepository;
    public UserServiceImpl(IUserRepository IUserRepository, EmailService emailService) {
        this.IUserRepository = IUserRepository;
    }

    public List<User> allUsers() {
        List<User> users = new ArrayList<>();
        IUserRepository.findAll().forEach(users::add);
        return users;
    }
}