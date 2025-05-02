package com.example.Learning_Course_App.controller;

import com.example.Learning_Course_App.dto.request.PaymentRequest;
import com.example.Learning_Course_App.dto.response.ApiResponse;
import com.example.Learning_Course_App.enumeration.ErrorCode;
import com.example.Learning_Course_App.enumeration.PaymentStatus;
import com.example.Learning_Course_App.service.IPaymentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final IPaymentService paymentService;

    @PostMapping("/create")
    public ApiResponse<String> createPayment(@RequestBody PaymentRequest request, HttpServletRequest httpRequest) {
        String ipAddress = httpRequest.getRemoteAddr();
        String paymentUrl = paymentService.createPaymentUrl(request, ipAddress);
        System.out.println(paymentUrl);
        return ApiResponse.success(ErrorCode.SUCCESS, paymentUrl);
    }

    @GetMapping("/vnpay-callback")
    public RedirectView vnpayCallback(HttpServletRequest request) {
        PaymentStatus status = paymentService.handleVNPayCallback(request);
        String redirectUrl = "myapp://payment-result?status=" + status.name().toLowerCase();
        return new RedirectView(redirectUrl);
    }
}