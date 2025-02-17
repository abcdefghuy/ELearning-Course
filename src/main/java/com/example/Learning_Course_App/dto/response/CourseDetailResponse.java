package com.example.Learning_Course_App.dto.response;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import com.example.Learning_Course_App.enumeration.CourseLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CourseDetailResponse {
    private Long courseId;
    private String courseName;
    private boolean enabled;
    private Double coursePrice;
    private String courseDescription;
    private Integer duration;
    private Date createdAt;
    private CourseLevel level;
    private Double rating;
    private Integer studentQuantity;
    private List<String> categoryNames;
}
