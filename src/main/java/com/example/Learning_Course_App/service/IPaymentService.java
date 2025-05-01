package com.example.Learning_Course_App.service;

import com.example.Learning_Course_App.dto.request.PaymentRequest;
import com.example.Learning_Course_App.enumeration.PaymentStatus;
import jakarta.servlet.http.HttpServletRequest;

public interface IPaymentService {
    public String createPaymentUrl(PaymentRequest request, String ipAddress);
    public PaymentStatus handleVNPayCallback(HttpServletRequest request);
}
