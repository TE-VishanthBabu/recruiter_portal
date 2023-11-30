package com.job_portal.recruiter_portal.controller;

import com.job_portal.recruiter_portal.entity.JobSeeker;
import com.job_portal.recruiter_portal.request.JobSeekerSearchRequest;
import com.job_portal.recruiter_portal.response.JobPortalResponse;
import com.job_portal.recruiter_portal.response.JobSeekerInfoResponse;
import com.job_portal.recruiter_portal.service.JobSeekerProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Locale;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/recruiters")
@Slf4j
public class JobSeekerProfileController {

    private final JobSeekerProfileService jobSeekerProfileService;
    private final MessageSource messageSource;

    /**
     * Get job-seeker profile information.
     *
     * @param jobSeekerId
     * @return job-seeker
     */
    @GetMapping("/job-seekers/{jobSeekerId}")
    public ResponseEntity<JobPortalResponse> getJobSeekerInfo(@PathVariable String jobSeekerId){
        JobSeekerInfoResponse jobSeeker = this.jobSeekerProfileService.getJobSeekerInfoByJobSeekerId(jobSeekerId);
        log.info("Gathered jobSeeker profile details for the Id: {}",jobSeekerId);
        JobPortalResponse response = new JobPortalResponse();
        response.setData(jobSeeker);
        response.setMessage(messageSource.getMessage("jobSeeker.add",null, Locale.getDefault()));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Get all Job-seekers
     * @return list of job-seekers
     */
    @GetMapping("/job-seekers")
    public ResponseEntity<JobPortalResponse> getAllJobSeeker() {
       List<JobSeeker> jobSeekers = this.jobSeekerProfileService.getAllJobSeeker();
       log.info("Gathered list of job-seekers");
       JobPortalResponse response = new JobPortalResponse();
       response.setData(jobSeekers);
       response.setMessage(messageSource.getMessage("getAll.jobSeeker",null,Locale.getDefault()));
       return new ResponseEntity<>(response,HttpStatus.OK);
    }

    /**
     * Search job-seeker
     * @param searchRequest
     * @return
     */
    @PostMapping("/job-seekers/search")
    public ResponseEntity<JobPortalResponse> searchJobSeeker(@RequestBody JobSeekerSearchRequest searchRequest) {
        Map<String,Object> jobSeeker = this.jobSeekerProfileService.searchJobSeeker(searchRequest);
        JobPortalResponse response = new JobPortalResponse();
        response.setData(jobSeeker);
        response.setMessage(messageSource.getMessage("search.jobSeeker",null, Locale.getDefault()));
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @GetMapping("/job-seekers/suggest")
    public ResponseEntity<JobPortalResponse> suggestJobSeekers(@RequestParam String keyword, @RequestParam String field) {
        List<String> jobSeekers = this.jobSeekerProfileService.suggestJobSeeker(keyword, field);
        JobPortalResponse jobPortalResponse = new JobPortalResponse();
        jobPortalResponse.setData(jobSeekers);
        return new ResponseEntity<>(jobPortalResponse, HttpStatus.OK);
    }
}
