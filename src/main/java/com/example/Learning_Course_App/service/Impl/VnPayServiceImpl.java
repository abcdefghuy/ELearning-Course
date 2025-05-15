package com.example.Learning_Course_App.service.Impl;

import com.example.Learning_Course_App.aop.ApiException;
import com.example.Learning_Course_App.dto.request.PaymentRequest;
import com.example.Learning_Course_App.entity.Course;
import com.example.Learning_Course_App.entity.Lesson;
import com.example.Learning_Course_App.entity.Payment;
import com.example.Learning_Course_App.entity.User;
import com.example.Learning_Course_App.enumeration.ErrorCode;
import com.example.Learning_Course_App.enumeration.PaymentProvider;
import com.example.Learning_Course_App.enumeration.PaymentStatus;
import com.example.Learning_Course_App.repository.ICourseRepository;
import com.example.Learning_Course_App.repository.IPaymentRepository;
import com.example.Learning_Course_App.repository.IUserRepository;
import com.example.Learning_Course_App.service.IEnrollmentService;
import com.example.Learning_Course_App.service.ILessonService;
import com.example.Learning_Course_App.service.IPaymentService;
import com.example.Learning_Course_App.service.IProgressService;
import com.example.Learning_Course_App.util.VNPayUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
@RequiredArgsConstructor
public class VnPayServiceImpl implements IPaymentService {

    @Value("${vnpay.tmnCode}")
    private String tmnCode;

    @Value("${vnpay.hashSecret}")
    private String hashSecret;

    @Value("${vnpay.payUrl}")
    private String payUrl;

    @Value("${vnpay.returnUrl}")
    private String returnUrl;

    private final IPaymentRepository paymentRepository;
    private final IUserRepository userRepository;
    private final ICourseRepository courseRepository;
    private final IEnrollmentService enrollmentService;
    private final IProgressService progressService;
    private final RedisService redisService;

    @Override
    public String createPaymentUrl(PaymentRequest request, String ipAddress) {
        try {
            String txnRef = UUID.randomUUID().toString().substring(0, 8);

            User user = userRepository.findById(request.getUserId())
                    .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));

            Course course = courseRepository.findById(request.getCourseId())
                    .orElseThrow(() -> new ApiException(ErrorCode.COURSE_NOT_FOUND));

            String orderInfo = "Payment for course: " + course.getCourseName();

            Map<String, String> params = buildVNPayParams(request, ipAddress, txnRef, orderInfo);

            String queryUrl = VNPayUtil.buildQueryUrl(params, true);
            String hashData = VNPayUtil.buildQueryUrl(params, false);
            String secureHash = VNPayUtil.hmacSHA512(hashSecret, hashData);

            String fullUrl = payUrl + "?" + queryUrl + "&vnp_SecureHash=" + secureHash;

            Payment payment = Payment.builder()
                    .student(user)
                    .course(course)
                    .amount(request.getAmount())
                    .provider(PaymentProvider.VNPAY)
                    .status(PaymentStatus.PENDING)
                    .orderId(txnRef)
                    .createdAt(new Date())
                    .build();

            paymentRepository.save(payment);
            return fullUrl;

        } catch (Exception e) {
            throw new ApiException(ErrorCode.INTERNAL_SERVER_ERROR, "Failed to create VNPay URL: " + e.getMessage());
        }
    }

    private Map<String, String> buildVNPayParams(PaymentRequest request, String ipAddress, String txnRef, String orderInfo) {
        Map<String, String> params = new HashMap<>();
        params.put("vnp_Version", "2.1.0");
        params.put("vnp_Command", "pay");
        params.put("vnp_TmnCode", tmnCode);
        params.put("vnp_Amount", String.valueOf((long) (request.getAmount() * 100)));
        params.put("vnp_CurrCode", "VND");
        params.put("vnp_TxnRef", txnRef);
        params.put("vnp_OrderInfo", orderInfo);
        params.put("vnp_OrderType", "other");
        params.put("vnp_Locale", "vn");
        params.put("vnp_ReturnUrl", returnUrl);
        params.put("vnp_IpAddr", ipAddress);
        params.put("vnp_CreateDate", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
        return params;
    }

    @Override
    public PaymentStatus handleVNPayCallback(HttpServletRequest request) {
        String txnRef = request.getParameter("vnp_TxnRef");
        String responseCode = request.getParameter("vnp_ResponseCode");
        String secureHash = request.getParameter("vnp_SecureHash");
        String transactionNo = request.getParameter("vnp_TransactionNo");

        if (txnRef == null || responseCode == null || secureHash == null) {
            throw new ApiException(ErrorCode.INVALID_REQUEST);
        }

        Map<String, String> fields = new HashMap<>();
        for (String key : request.getParameterMap().keySet()) {
            if (!key.equals("vnp_SecureHash") && !key.equals("vnp_SecureHashType")) {
                fields.put(key, request.getParameter(key));
            }
        }

        try {
            String hashData = VNPayUtil.buildQueryUrl(fields, false);
            String calculatedHash = VNPayUtil.hmacSHA512(hashSecret, hashData);

            if (!calculatedHash.equals(secureHash)) {
                throw new ApiException(ErrorCode.INVALID_SIGNATURE);
            }

            Payment payment = paymentRepository.findByOrderId(txnRef)
                    .orElseThrow(() -> new ApiException(ErrorCode.ORDER_NOT_FOUND));

            PaymentStatus status = "00".equals(responseCode) ? PaymentStatus.SUCCESS : PaymentStatus.FAILED;
            payment.setStatus(status);
            payment.setTransactionNo(transactionNo);
            paymentRepository.save(payment);

            if (status == PaymentStatus.SUCCESS) {
                enrollmentService.enrollUser(payment.getStudent(), payment.getCourse());
                redisService.delete("continue_course_user:" + payment.getStudent().getId());

                progressService.initCourseProgress(payment.getStudent(), payment.getCourse());
            }

            return status;
        } catch (Exception e) {
            throw new ApiException(ErrorCode.INTERNAL_SERVER_ERROR, "VNPay xử lý lỗi: " + e.getMessage());
        }
    }
}
