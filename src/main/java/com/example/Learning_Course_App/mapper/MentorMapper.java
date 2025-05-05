package com.example.Learning_Course_App.mapper;

import com.example.Learning_Course_App.dto.response.MentorResponse;
import com.example.Learning_Course_App.entity.Mentor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class MentorMapper {
     public MentorResponse toMentorResponse(Mentor mentor) {
         return MentorResponse.builder()
                 .id(mentor.getId())
                 .mentorName(mentor.getMentorName())
                 .mentorAvatar(mentor.getMentorAvatar())
                 .build();
     }


}
