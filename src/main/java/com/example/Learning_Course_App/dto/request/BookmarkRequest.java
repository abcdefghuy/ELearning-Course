package com.example.Learning_Course_App.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookmarkRequest {
    @NotNull(message = "Course ID is required")
    private Long courseId;
}
