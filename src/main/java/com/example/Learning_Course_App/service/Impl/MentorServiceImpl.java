package com.example.Learning_Course_App.service.Impl;

import com.example.Learning_Course_App.dto.response.CourseResponse;
import com.example.Learning_Course_App.dto.response.MentorResponse;
import com.example.Learning_Course_App.entity.Course;
import com.example.Learning_Course_App.entity.Mentor;
import com.example.Learning_Course_App.mapper.MentorMapper;
import com.example.Learning_Course_App.repository.IMentorRepository;
import com.example.Learning_Course_App.service.IMentorService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MentorServiceImpl implements IMentorService {
    private final IMentorRepository mentorRepository;
    private final MentorMapper mentorMapper;
    private final RedisService redisService;
    public MentorServiceImpl(IMentorRepository mentorRepository, MentorMapper mentorMapper, RedisService redisService) {
        this.mentorRepository = mentorRepository;
        this.mentorMapper = mentorMapper;
        this.redisService = redisService;
    }
    @Override
    public Page<MentorResponse> getTop10BestMentor() {
        List<MentorResponse> cachedMentors = redisService.getList("top10_mentor", MentorResponse.class);
        if (cachedMentors != null && !cachedMentors.isEmpty()) {
            return paginateMentor(cachedMentors,0, 10); // nếu cached toàn bộ thì dùng hàm này
        }
        Pageable pageable = PageRequest.of(0, 10);
        Page<Mentor> topCourses = mentorRepository.findTop10BestMentor(pageable);
        List<MentorResponse> mentorDTOs = topCourses.map(mentorMapper::toMentorResponse).getContent();
        redisService.saveList("top10_course", mentorDTOs, 10);
        return new PageImpl<>(mentorDTOs, pageable, topCourses.getTotalElements());
    }

    @Override
    public Page<MentorResponse> getAllMentor(PageRequest of) {
        List<MentorResponse> cachedMentors = redisService.getList("all_mentor", MentorResponse.class);
        if (cachedMentors != null && !cachedMentors.isEmpty()) {
            return paginateMentor(cachedMentors,of.getPageNumber(), of.getPageSize()); // nếu cached toàn bộ thì dùng hàm này
        }
        Page<Mentor> mentors = mentorRepository.findAll(of);
        List<MentorResponse> mentorDTOs = mentors.map(mentorMapper::toMentorResponse).getContent();
        redisService.saveList("all_mentor", mentorDTOs, 10);
        return new PageImpl<>(mentorDTOs, of, mentors.getTotalElements());
    }

    private Page<MentorResponse> paginateMentor(List<MentorResponse> cachedMentors, int page, int size) {
        int start = page  * size;
        int end = Math.min(start + size, cachedMentors.size());
        return new PageImpl<>(cachedMentors.subList(start, end), PageRequest.of(page , size), cachedMentors.size());
    }
}
