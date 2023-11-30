package com.job_portal.recruiter_portal.service;

import com.job_portal.recruiter_portal.customException.CommonException;
import com.job_portal.recruiter_portal.entity.CompanyProfile;
import com.job_portal.recruiter_portal.entity.JobPost;
import com.job_portal.recruiter_portal.entity.Recruiter;
import com.job_portal.recruiter_portal.repository.CompanyProfileRepository;
import com.job_portal.recruiter_portal.repository.JobPostRepository;
import com.job_portal.recruiter_portal.repository.RecruiterRepository;
import com.job_portal.recruiter_portal.request.CompanyProfileRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
@Slf4j
@Validated
@RequiredArgsConstructor
public class CompanyProfileService {

    private final CompanyProfileRepository profileRepository;
    private final RecruiterRepository recruiterRepository;
    private final MessageSource messageSource;
    private final JobPostRepository jobPostRepository;

    /**
     * Creating company profile.
     *
     * @param recruiterId
     * @param request   Company profile DTO
     * @return company profile
     */
    public CompanyProfile createCompanyProfile(String recruiterId, CompanyProfileRequest request){
        CompanyProfile profile = profileRepository.findByRecruiterId(recruiterId);
        if(profile!=null) {
            return this.setCompanyProfile(request,profile,recruiterId);
        } else {
             return this.setCompanyProfile(request,new CompanyProfile(),recruiterId);
        }
    }

    /**
     * Get company profile details by recruiter Id.
     *
     * @param recruiterId
     * @return company profile
     */
    public CompanyProfile getCompanyProfile(String recruiterId) {
        return profileRepository.findByRecruiterId(recruiterId);
    }

    /**
     * Setting company profile details.
     *
     * @param request   Company profile DTO
     * @param profile   Company profile
     * @param recruiterId
     * @return company profile.
     */
    public CompanyProfile setCompanyProfile(CompanyProfileRequest request,CompanyProfile profile,String recruiterId) {
        profile.setCompanyProfileInfo(request.getCompanyProfileInfo());
        profile.setCompanyPhoto(request.getCompanyPhoto());
        profile.setFounded(request.getFounded());
        profile.setEmployee(request.getEmployee());
        profile.setLocation(request.getLocation());
        profile.setUrl(request.getUrl());
        profile.setContact(request.getContact());
        profile.setTechStack(request.getTechStack());
        profile.setIndustryType(request.getIndustryType());
        Recruiter recruiter = recruiterRepository.findById(recruiterId).orElseThrow(()->{
           throw new CommonException(messageSource.getMessage("recruiter.notFound",null,Locale.getDefault()),HttpStatus.NOT_FOUND);
        });
        if(request.getCompanyName()!=null) {
            recruiter.setCompanyName(request.getCompanyName());
        }
        profile.setRecruiter(recruiter);
        List<JobPost> jobPosts = new ArrayList<>();
        for(JobPost post:recruiter.getJobPost()) {
            JobPost jobPost = jobPostRepository.findById(post.getId()).orElseThrow(()->{
                throw new CommonException(messageSource.getMessage("jobPost.notFound",null,Locale.getDefault()),HttpStatus.NOT_FOUND);
            });
            jobPosts.add(jobPost);
        }
        profile.setJobPosts(jobPosts);
        profile.setOfficeLocation(request.getOfficeLocation());
        return profileRepository.save(profile);
    }
}
