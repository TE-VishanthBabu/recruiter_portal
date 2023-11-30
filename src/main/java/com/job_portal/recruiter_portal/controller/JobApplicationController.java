package com.job_portal.recruiter_portal.controller;

import com.job_portal.recruiter_portal.entity.JobApplication;
import com.job_portal.recruiter_portal.request.JobApplicationRequest;
import com.job_portal.recruiter_portal.response.JobPortalResponse;
import com.job_portal.recruiter_portal.service.JobApplicationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.util.Locale;
import java.util.Set;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/recruiters")
public class JobApplicationController {

    private final JobApplicationService applicationService;
    private final MessageSource messageSource;


    /**
     * Updating job-application details.
     *
     * @param jobApplicationId
     * @param request   job-application DTO
     * @return updated job-application
     */
    @PutMapping("/job-applications/{jobApplicationId}")
    public ResponseEntity<JobPortalResponse> updateJobApplication(@PathVariable String jobApplicationId,
                                                                  @RequestBody(required = false) JobApplicationRequest request) {
        JobApplication jobApplication = this.applicationService.updateJobApplication(jobApplicationId,request);
        log.info("Job-application updated");
        JobPortalResponse response = new JobPortalResponse();
        response.setData(jobApplication);
        response.setMessage(messageSource.getMessage("update.job-application",null,Locale.getDefault()));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Delete job-application by job-application id.
     *
     * @param jobApplicationId
     * @return deleted job-application response
     */
    @DeleteMapping("/job-applications/{jobApplicationId}")
    public ResponseEntity<JobPortalResponse> deleteJobApplication(@PathVariable String jobApplicationId) {
        this.applicationService.deleteJobApplication(jobApplicationId);
        JobPortalResponse response = new JobPortalResponse();
        response.setMessage(messageSource.getMessage("delete.job-application",null,Locale.getDefault()));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Get jobApplication by id.
     *
     * @param jobApplicationId
     * @return job-application
     */
    @GetMapping("/job-applications/{jobApplicationId}")
    public ResponseEntity<JobPortalResponse> getJobApplication(@PathVariable String jobApplicationId) {
        JobApplication application = this.applicationService.getJobApplicationById(jobApplicationId);
        JobPortalResponse response = new JobPortalResponse();
        response.setData(application);
        response.setMessage(messageSource.getMessage("get.jobApplication",null,Locale.getDefault()));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("{recruiterId}/job-posts/positions")
    public ResponseEntity<JobPortalResponse> getAllPositionsApplied(@PathVariable String recruiterId) {
        Set<String> positions = this.applicationService.getAllPositions(recruiterId);
        JobPortalResponse response = new JobPortalResponse();
        response.setData(positions);
        response.setMessage(messageSource.getMessage("get.positions",null,Locale.getDefault()));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{recruiterId}/job-applications")
    public ResponseEntity<JobPortalResponse> getMyApplicants(@PathVariable String recruiterId) {
        JobPortalResponse response = new JobPortalResponse();
        response.setData(this.applicationService.getMyApplicants(recruiterId));
        response.setMessage(messageSource.getMessage("myApplicants",null,Locale.getDefault()));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
