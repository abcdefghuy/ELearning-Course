# 📚 ELearning-Course (Backend)

**ELearning-Course** là hệ thống backend RESTful API hỗ trợ cho nền tảng học trực tuyến, được xây dựng bằng **Java Spring Boot**. Dự án hỗ trợ các tính năng học tập hiện đại như quản lý khóa học, bài học, thanh toán VNPay, xác thực người dùng qua OTP và theo dõi tiến độ học tập.

---

## 🚀 Tính năng dành cho người dùng

### 👤 Quản lý người dùng
  - Quản lý thông tin cá nhân
  - Đăng ký / đăng nhập / quên mật khẩu (JWT + OTP)
### 🎓 Học tập và tương tác
  - Xem thông tin chi tiết khóa học
  - Tìm kiếm khóa học
  - Xem video bài giảng
  - Đánh giá và bình luận khóa học
  - Chức năng bookmark / yêu thích
  - Trả lời câu hỏi bài tập sau video
  - Cung cấp chứng chỉ hoàn thành khóa học
### 💳 Thanh toán
  - Tích hợp VNPay
  - Xác minh callback thanh toán
### ✉️ Gửi email hệ thống
  - Gửi OTP, xác nhận thanh toán, v.v.

---

## ⚙️ Công nghệ sử dụng

- Java 17
- Spring Boot 3.x
- Spring Security (JWT)
- Spring Data JPA (MySQL)
- Swagger UI (OpenAPI)
- Lombok, MapStruct
- Redis (Lưu OTP)
- VNPay Gateway (tích hợp thanh toán)
- Mail SMTP (Gmail)
- Cloudinary (tùy chọn, upload ảnh)

---

## 📁 Cấu trúc thư mục chính

```
src/
├── controller/         # Các REST API Endpoint
├── service/            # Xử lý nghiệp vụ
├── repository/         # JPA Repository
├── entity/             # Các Entity ánh xạ database
├── dto/                # DTO Request/Response
├── config/             # Bảo mật, Swagger, cấu hình chung
├── aop/                # Exception handler
├── utils/              # Các hàm tiện ích: JWT, Email, VNPay...
└── resources/
    └── application.properties    # Cấu hình đọc từ .env
```

---

## 🔧 Cấu hình `application.properties`

```properties
spring.application.name=learning-course-app
spring.config.import=optional:file:.env[.properties]

spring.datasource.url=${SPRING_DATASOURCE_URL}
spring.datasource.username=${SPRING_DATASOURCE_USERNAME}
spring.datasource.password=${SPRING_DATASOURCE_PASSWORD}
spring.jpa.hibernate.ddl-auto=update

security.jwt.secret-key=${JWT_SECRET_KEY}
security.jwt.expiration-time=3600000

spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=${SUPPORT_EMAIL}
spring.mail.password=${APP_PASSWORD}
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true

spring.data.redis.host=${REDIS_CLOUD_HOST}
spring.data.redis.port=${REDIS_CLOUD_PORT}
spring.data.redis.password=${REDIS_CLOUD_PASSWORD}

vnpay.tmnCode=${VNPAY_TMN_CODE}
vnpay.hashSecret=${VNPAY_SECRET_KEY}
vnpay.payUrl=https://sandbox.vnpayment.vn/paymentv2/vpcpay.html
vnpay.returnUrl=${VNPAY_RETURN_URL}
```

> 🔐 Dự án sử dụng biến môi trường `.env` để đảm bảo bảo mật cho thông tin nhạy cảm.

---

## 🏁 Hướng dẫn chạy dự án

1. Clone repo:
```bash
git clone https://github.com/abcdefghuy/ELearning-Course.git
cd ELearning-Course
```

2. Cấu hình file `.env` chứa các biến môi trường như:
```env
SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/elearning
SPRING_DATASOURCE_USERNAME=root
SPRING_DATASOURCE_PASSWORD=yourpassword
JWT_SECRET_KEY=your_secret_key
SUPPORT_EMAIL=your_email@gmail.com
APP_PASSWORD=your_app_password
REDIS_CLOUD_HOST=localhost
REDIS_CLOUD_PORT=6379
REDIS_CLOUD_PASSWORD=
VNPAY_TMN_CODE=...
VNPAY_SECRET_KEY=...
VNPAY_RETURN_URL=https://<public-domain>/api/payment/vnpay-callback  # ⚠️ Không dùng localhost, phải là URL công khai (vd: ngrok, Render...)
```

3. Import file SQL `db_elearning_course.sql` vào MySQL để khởi tạo dữ liệu.

4. Mở dự án bằng **IntelliJ IDEA** > Nhấn **Run** hoặc dùng Gradle/Maven panel để chạy `Application.java`.

5. Truy cập Swagger để test API:
```
http://localhost:8080/swagger-ui/index.html
```

---

## 📌 Một số API tiêu biểu

| Method | Endpoint | Mô tả |
|--------|----------|-------|
| POST | `/api/auth/register` | Đăng ký tài khoản |
| POST | `/api/auth/verify-otp` | Xác thực OTP |
| POST | `/api/auth/login` | Đăng nhập lấy JWT |
| GET  | `/api/courses` | Lấy danh sách khóa học |
| POST | `/api/payment/create` | Tạo link thanh toán VNPay |
| GET  | `/api/payment/vnpay-callback` | Callback từ VNPay |
| GET  | `/api/progress/{courseId}` | Xem tiến độ học tập |

---

## 👨‍🎓 Sinh viên thực hiện

- **Lê Nhựt Anh** - 22110279  
- **Nguyễn Sang Huy** - 22110333  

---

> © 2025 - ELearning Team | Repo: [github.com/abcdefghuy/ELearning-Course](https://github.com/abcdefghuy/ELearning-Course)
