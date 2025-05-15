package com.example.Learning_Course_App.service.Impl;


import com.example.Learning_Course_App.aop.ApiException;
import com.example.Learning_Course_App.dto.request.UserRequest;
import com.example.Learning_Course_App.dto.response.CertificateResponse;
import com.example.Learning_Course_App.dto.response.UserDetailResponse;
import com.example.Learning_Course_App.entity.Course;
import com.example.Learning_Course_App.entity.User;
import com.example.Learning_Course_App.enumeration.ErrorCode;
import com.example.Learning_Course_App.mapper.UserMapper;
import com.example.Learning_Course_App.repository.ICourseRepository;
import com.example.Learning_Course_App.repository.IUserRepository;
import com.example.Learning_Course_App.service.IUserService;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class UserServiceImpl implements IUserService {
    private final IUserRepository IUserRepository;
    private final UserMapper userMapper;
    private final RedisService redisService;
    private final ICourseRepository courseRepository;
    public UserServiceImpl(IUserRepository IUserRepository, EmailService emailService, UserMapper userMapper, RedisService redisService, ICourseRepository courseRepository) {
        this.IUserRepository = IUserRepository;
        this.userMapper = userMapper;
        this.redisService = redisService;
        this.courseRepository = courseRepository;
    }

    public List<User> allUsers() {
        List<User> users = new ArrayList<>();
        IUserRepository.findAll().forEach(users::add);
        return users;
    }

    @Override
    public UserDetailResponse getUserInfo(Long userId) {
        redisService.delete("user_" + userId);
        String cacheKey = "user_" + userId;
        if (redisService.exists(cacheKey)) {
            UserDetailResponse cachedUser = redisService.get(cacheKey, UserDetailResponse.class);
            if (cachedUser != null) {
                return cachedUser;
            }
        }
        User user = IUserRepository.findById(userId).orElse(null);
        if (user == null) {
            return null; // or throw an exception
        }
        UserDetailResponse userDetailResponse = userMapper.toDTO(user);
        redisService.save(cacheKey, userDetailResponse, 60);
        return userDetailResponse;
    }

    @Override
    public void updateUserInfo(UserRequest userRequest, Long userId) {
        // Kiểm tra cache của user có tồn tại không và xóa nó
        String cacheKey = "user_" + userId;
        if (redisService.exists(cacheKey)) {
            redisService.delete(cacheKey);
        }

        // Tìm kiếm user trong cơ sở dữ liệu
        User userEntity = IUserRepository.findById(userId)
                .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND, "User not found"));

        // Cập nhật thông tin user
        updateUserInfo(userEntity, userRequest);

        // Lưu thông tin user đã cập nhật
        IUserRepository.save(userEntity);
    }

    private void updateUserInfo(User userEntity, UserRequest userRequest) {
        try {
            // Cập nhật các thông tin cơ bản của user
            userEntity.setFullName(userRequest.getFullName());
            userEntity.setPhoneNumber(userRequest.getPhone());
            userEntity.setAvatarUrl(userRequest.getAvatarUrl());
            userEntity.setGender(userRequest.getGender());

            // Xử lý ngày sinh (birthday)
            if (userRequest.getBirthday() != null && !userRequest.getBirthday().isEmpty()) {
                // Định dạng đầu vào
                SimpleDateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy");
                Date date = inputFormat.parse(userRequest.getBirthday());

                // Định dạng đầu ra (yyyy-MM-dd)
                SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
                String formattedBirthday = outputFormat.format(date);  // Định dạng lại ngày

                // Chuyển đổi sang kiểu Date mới
                Date birthday = outputFormat.parse(formattedBirthday);
                userEntity.setDateOfBirth(birthday);
            }
        } catch (ParseException e) {
            throw new ApiException(ErrorCode.INVALID_DATE_FORMAT, "Invalid date format for birthday");
        }
    }

    @Override
    public CertificateResponse getUserCertificates(Long userId, Long courseId) {
        User user = IUserRepository.findById(userId).orElse(null);
        if (user == null) {
            return null; // or throw an exception
        }
        Course course = courseRepository.findById(courseId).orElse(null);
        if (course == null) {
            return null; // or throw an exception
        }
        CertificateResponse certificateResponse = new CertificateResponse();
        certificateResponse.setUserName(user.getFullName());
        certificateResponse.setCourseName(course.getCourseName());
        // Lấy thời gian hiện tại
        Date currentDate = new Date();
        // Định dạng ngày tháng
        certificateResponse.setCreatedAt(currentDate);
        certificateResponse.setCertificateId(generateCertificateId());
        return certificateResponse;
    }
    public String generateCertificateId() {
        // Sử dụng UUID để tạo một ID ngẫu nhiên duy nhất
        return "CERT-" + UUID.randomUUID().toString().substring(0, 8);
    }

}