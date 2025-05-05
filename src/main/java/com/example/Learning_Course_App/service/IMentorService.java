package com.example.Learning_Course_App.service;

import com.example.Learning_Course_App.dto.response.MentorResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface IMentorService {
    Page<MentorResponse> getTop10BestMentor();

    Page<MentorResponse> getAllMentor(PageRequest of);
}
