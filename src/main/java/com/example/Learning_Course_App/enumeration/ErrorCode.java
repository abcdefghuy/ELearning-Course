package com.example.Learning_Course_App.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // Success Codes (1xxx)
    SUCCESS(1000, "Request processed successfully"),
    CREATED(1001, "Created successfully"),
    UPDATED(1002, "Data updated successfully"),
    DELETED(1003, "Data deleted successfully"),
    LOGIN_SUCCESS(1004, "Login successful"),
    SUCCESS_ACCOUNT_VERIFIED(1005, "Account has already been verified successfully"),
    SUCCESS_OTP_SENT(1006, "OTP has been sent to your email address"),
    SUCCESS_PASSWORD_RESET(1007, "Password has been reset successfully"),

    // Authentication & Authorization Errors (2xxx)
    INVALID_CREDENTIALS(2000, "Invalid email or password"),
    ACCOUNT_NOT_VERIFIED(2001, "Account not verified. Please verify your account."),
    TOKEN_EXPIRED(2002, "Token expired"),
    TOKEN_MISSING(2003, "Token missing"),
    ACCESS_DENIED(2004, "Access denied"),

    // Bad Request & Validation Errors (3xxx)
    INVALID_REQUEST_PARAMETERS(3000, "Invalid request parameters"),
    MISSING_REQUIRED_PARAMETER(3001, "Missing required parameter"),
    INVALID_EMAIL_FORMAT(3002, "Please provide a valid email address."),
    PASSWORD_LENGTH_INVALID(3003, "Password length must be between 8 and 30 characters."),
    PASSWORD_INVALID_FORMAT(3004, "Password must contain at least one letter, one number, and one special character."),
    INVALID_OTP_FORMAT(3006, "OTP must be 6 digits."),

    // Data Errors (4xxx)
    USER_NOT_FOUND(4000, "User not found"),
    EMAIL_ALREADY_REGISTERED(4001, "Email is already registered"),
    ACCOUNT_ALREADY_VERIFIED(4002, "Account is already verified"),
    OTP_EXPIRED(4003, "OTP has expired"),
    OTP_STILL_VALID(4005, "OTP is still valid. Please wait until it expires."),
    INVALID_OTP(4007, "Invalid OTP. Please check the code and try again."),
    EXPIRED_OTP(4008, "OTP has expired. Please request a new OTP."),
    CATEGORY_NOT_FOUND(4009, "Category not found"),
    COURSE_NOT_FOUND(4010, "Course not found"),

    // Server Errors (5xxx)
    INTERNAL_SERVER_ERROR(5000, "An unexpected error occurred. Please try again later."),
    USER_REGISTRATION_FAILED(5001, "User registration failed"),
    USER_VERIFICATION_FAILED(5002, "User verification failed"),
    OTP_RESEND_FAILED(5003, "Failed to resend OTP"),
    REDIS_ERROR(5004, "Redis error occurred");

    private final int code;
    private final String message;
}
