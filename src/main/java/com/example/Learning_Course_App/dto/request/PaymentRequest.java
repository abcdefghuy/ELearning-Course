package com.example.Learning_Course_App.dto.request;

import com.example.Learning_Course_App.enumeration.PaymentProvider;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentRequest {
    private Long userId;
    private Long courseId;
    private double amount;
    private PaymentProvider provider;
}
