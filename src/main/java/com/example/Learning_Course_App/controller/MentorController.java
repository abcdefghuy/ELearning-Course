package com.example.Learning_Course_App.controller;

import com.example.Learning_Course_App.dto.response.ApiResponse;
import com.example.Learning_Course_App.dto.response.CourseResponse;
import com.example.Learning_Course_App.dto.response.MentorResponse;
import com.example.Learning_Course_App.dto.response.PagedResponse;
import com.example.Learning_Course_App.enumeration.ErrorCode;
import com.example.Learning_Course_App.service.IMentorService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/mentors")
public class MentorController {
    private final IMentorService mentorService;

    public MentorController(IMentorService mentorService) {
        this.mentorService = mentorService;
    }

    @GetMapping("/top-mentors")
    public ResponseEntity<?> getTopMentor(){
        Page<MentorResponse> mentors = mentorService.getTop10BestMentor();
        PagedResponse<MentorResponse> response = new PagedResponse<>(
                mentors.getContent(), mentors.getNumber(), mentors.getSize(),
                mentors.getTotalElements(), mentors.getTotalPages(), mentors.isLast()
        );
        return ResponseEntity.ok(ApiResponse.success(ErrorCode.SUCCESS, response));
    }
    @GetMapping("/all")
    public ResponseEntity<?> getAllMentor(@RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "10") int size){
        Page<MentorResponse> mentors = mentorService.getAllMentor(PageRequest.of(page, size));
        PagedResponse<MentorResponse> response = new PagedResponse<>(
                mentors.getContent(), mentors.getNumber(), mentors.getSize(),
                mentors.getTotalElements(), mentors.getTotalPages(), mentors.isLast()
        );
        return ResponseEntity.ok(ApiResponse.success(ErrorCode.SUCCESS, response));
    }

}
