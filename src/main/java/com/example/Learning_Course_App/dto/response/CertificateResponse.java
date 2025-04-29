package com.example.Learning_Course_App.dto.response;

import lombok.*;

import java.util.Date;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CertificateResponse {
    private String certificateId;
    private String userName;
    private String courseName;
    private Date createdAt;
}
