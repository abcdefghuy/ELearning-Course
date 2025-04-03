package com.example.Learning_Course_App.service.Impl;

import com.example.Learning_Course_App.aop.ApiException;
import com.example.Learning_Course_App.dto.request.*;
import com.example.Learning_Course_App.dto.response.LoginResponse;
import com.example.Learning_Course_App.entity.User;
import com.example.Learning_Course_App.enumeration.ErrorCode;
import com.example.Learning_Course_App.enumeration.Role;
import com.example.Learning_Course_App.repository.IUserRepository;
import jakarta.mail.MessagingException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.Random;

@Service
public class AuthenticationService {
    private final IUserRepository IUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;
    private final RedisService redisService;
    private final JwtService jwtService;

    public AuthenticationService(
            IUserRepository IUserRepository,
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder,
            EmailService emailService,
            RedisService redisService,
            JwtService jwtService
    ) {
        this.authenticationManager = authenticationManager;
        this.IUserRepository = IUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.redisService = redisService;
        this.jwtService = jwtService;
    }

    public void signup(RegisterUserRequest input) {
        if (IUserRepository.existsByEmail(input.getEmail())) {
            throw new ApiException(ErrorCode.EMAIL_ALREADY_REGISTERED);
        }

        try {
            String encodedPassword = passwordEncoder.encode(input.getPassword());

            User user = new User();
            user.setEmail(input.getEmail());
            user.setPassword(encodedPassword);
            user.setRole(Role.STUDENT);
            user.setCreatedAt(LocalDateTime.now());
            user.setUpdatedAt(LocalDateTime.now());
            user.setEnabled(false);

            IUserRepository.save(user);

            String otp = generateVerificationCode();
            redisService.save(input.getEmail(), otp, 15);
            sendVerificationEmail(input.getEmail(), otp);
        } catch (Exception e) {
            throw new ApiException(ErrorCode.USER_REGISTRATION_FAILED);
        }
    }


    public LoginResponse authenticate(LoginUserRequest input) {
        User user = IUserRepository.findByEmail(input.getEmail())
                .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));

        if (!user.isEnabled()) {
            throw new ApiException(ErrorCode.ACCOUNT_NOT_VERIFIED);
        }

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(input.getEmail(), input.getPassword())
            );
        } catch (BadCredentialsException e) {
            throw new ApiException(ErrorCode.INVALID_CREDENTIALS);
        }

        String jwtToken = jwtService.generateToken(user);
        long expiresIn = jwtService.getExpirationTime();

        return new LoginResponse(jwtToken, expiresIn);
    }


    public void verifyUser(VerifyUserRequest input) {
        User user = IUserRepository.findByEmail(input.getEmail())
                .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));

        if (user.isEnabled()) {
            throw new ApiException(ErrorCode.ACCOUNT_ALREADY_VERIFIED);
        }

        if (!redisService.exists(input.getEmail())) {
            throw new ApiException(ErrorCode.OTP_EXPIRED);
        }

        String storedCode = redisService.get(input.getEmail());

        if (storedCode == null || !storedCode.equals(input.getVerificationCode())) {
            throw new ApiException(ErrorCode.INVALID_OTP);
        }

        try {
            user.setEnabled(true);
            user.setUpdatedAt(LocalDateTime.now());
            IUserRepository.save(user);
            redisService.delete(input.getEmail());
        } catch (Exception e) {
            throw new ApiException(ErrorCode.USER_VERIFICATION_FAILED);
        }
    }

    public void resendVerificationCode(String email) {
        User user = IUserRepository.findByEmail(email)
                .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));

        if (user.isEnabled()) {
            throw new ApiException(ErrorCode.ACCOUNT_ALREADY_VERIFIED);
        }

        if (redisService.exists(email)) {
            throw new ApiException(ErrorCode.OTP_STILL_VALID);
        }

        try {
            String newOtp = generateVerificationCode();
            redisService.save(email, newOtp, 15);
            sendVerificationEmail(email, newOtp);
        } catch (Exception e) {
            throw new ApiException(ErrorCode.OTP_RESEND_FAILED);
        }
    }


    private void sendVerificationEmail(String email, String otp) { //TODO: Update with company logo
        String subject = "Account Verification";
        String verificationCode = "VERIFICATION CODE " + otp;
        String htmlMessage = "<html>"
                + "<body style=\"font-family: Arial, sans-serif;\">"
                + "<div style=\"background-color: #f5f5f5; padding: 20px;\">"
                + "<h2 style=\"color: #333;\">Welcome to our app!</h2>"
                + "<p style=\"font-size: 16px;\">Please enter the verification code below to continue:</p>"
                + "<div style=\"background-color: #fff; padding: 20px; border-radius: 5px; box-shadow: 0 0 10px rgba(0,0,0,0.1);\">"
                + "<h3 style=\"color: #333;\">Verification Code:</h3>"
                + "<p style=\"font-size: 18px; font-weight: bold; color: #007bff;\">" + verificationCode + "</p>"
                + "</div>"
                + "</div>"
                + "</body>"
                + "</html>";

        try {
            emailService.sendVerificationEmail(email, subject, htmlMessage);
        } catch (MessagingException e) {
            // Handle email sending exception
            e.printStackTrace();
        }
    }

    public void forgotPassword(ForgotPasswordRequest input) {
        User user = IUserRepository.findByEmail(input.getEmail())
                .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));

        if (!user.isEnabled()) {
            throw new ApiException(ErrorCode.ACCOUNT_NOT_VERIFIED);
        }

        try {
            String otp = generateVerificationCode();
            redisService.save(input.getEmail(), otp, 15);
            sendVerificationEmail(input.getEmail(), otp);
        } catch (RuntimeException e) {
            throw new ApiException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    public void resetPassword(ResetPasswordRequest input) {
        User user = IUserRepository.findByEmail(input.getEmail())
                .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));

        if (!user.isEnabled()) {
            throw new ApiException(ErrorCode.ACCOUNT_NOT_VERIFIED);
        }

        try {
            user.setPassword(passwordEncoder.encode(input.getNewPassword()));
            IUserRepository.save(user);
        } catch (RuntimeException e) {
            throw new ApiException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    private String generateVerificationCode() {
        Random random = new Random();
        int code = random.nextInt(900000) + 100000;
        return String.valueOf(code);
    }
}
