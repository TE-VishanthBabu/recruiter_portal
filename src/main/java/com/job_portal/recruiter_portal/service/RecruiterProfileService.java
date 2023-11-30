package com.job_portal.recruiter_portal.service;

import com.job_portal.recruiter_portal.customException.CommonException;
import com.job_portal.recruiter_portal.entity.Recruiter;
import com.job_portal.recruiter_portal.repository.RecruiterRepository;
import com.job_portal.recruiter_portal.request.RecruiterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RecruiterProfileService {
    private final RecruiterRepository recruiterRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    public Recruiter updateRecruiterProfile(String recruiterId, RecruiterRequest request) {
        Recruiter recruiter = recruiterRepository.findById(recruiterId).orElseThrow(()->{
            throw new CommonException("Recruiter id not found", HttpStatus.NOT_FOUND);
        });
        recruiter.setUserName(request.getUserName());
        recruiter.setFirstName(request.getFirstName());
        recruiter.setLastName(request.getLastName());
        recruiter.setEmail(request.getEmail());
        if(request.getPassword().equals(request.getConfirmPassword())) {
            recruiter.setPassword(passwordEncoder.encode(request.getPassword()));
        } else {
            throw new CommonException("Password Mismatch",HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return recruiterRepository.save(recruiter);
    }

    public Recruiter getRecruiterProfile(String recruiterId) {
        return recruiterRepository.findById(recruiterId).orElseThrow(()->{
            throw new CommonException("Recruiter Id not found",HttpStatus.NOT_FOUND);
        });
    }
}
