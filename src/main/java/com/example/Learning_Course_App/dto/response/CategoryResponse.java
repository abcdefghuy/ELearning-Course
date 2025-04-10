package com.example.Learning_Course_App.dto.response;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryResponse {
    private int categoryId;
    private String categoryName;
}
