package com.example.Learning_Course_App.dto.response;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import com.example.Learning_Course_App.enumeration.CourseLevel;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseDetailResponse {
    private long courseId;
    private String courseName;
    private boolean enabled;
    private Double coursePrice;
    private String courseImg;
    private String courseDescription;
    private int duration;
    private Date createdAt;
    private Date updateAt;
    private String level;
    private Double rating;
    private int lessonCount;
    private int reviewCount;
    private int studentQuantity;
    private List<String> categoryNames;
    private boolean isBestSeller;
    private boolean isBookmarked;
}
