package com.example.Learning_Course_App.dto.response;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class MentorResponse {
    private Long id;
    private String mentorName;
    private String mentorAvatar;
}
