package com.job_portal.recruiter_portal.controller;

import com.job_portal.recruiter_portal.response.JobPortalResponse;
import com.job_portal.recruiter_portal.response.MyApplicationResponse;
import com.job_portal.recruiter_portal.service.JobApplicationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Locale;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/recruiters")
public class DashboardController {

    private final JobApplicationService applicationService;
    private final MessageSource messageSource;

    @GetMapping("/{recruiterId}/job-application")
    public ResponseEntity<JobPortalResponse> getAllJobApplication(@RequestParam(required = false) @DateTimeFormat(pattern="yyyy-MM-dd") Date startDate,
                                                                  @RequestParam(required = false) @DateTimeFormat(pattern="yyyy-MM-dd") Date endDate,
                                                                  @PathVariable String recruiterId) {
        List<MyApplicationResponse> application = this.applicationService.getAllJobApplication(startDate,endDate,recruiterId);
        JobPortalResponse response = new JobPortalResponse();
        response.setData(application);
        response.setMessage(messageSource.getMessage("getAll.application",null, Locale.getDefault()));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{recruiterId}/job-application/count")
    public ResponseEntity<JobPortalResponse> getAllApplicationCount(@RequestParam(required = false) @DateTimeFormat(pattern="yyyy-MM-dd") Date startDate,
                                                                    @RequestParam(required = false) @DateTimeFormat(pattern="yyyy-MM-dd") Date endDate,
                                                                    @PathVariable String recruiterId) {
        MyApplicationResponse response = this.applicationService.getApplicationCount(startDate,endDate,recruiterId);
        JobPortalResponse jobPortalResponse = new JobPortalResponse();
        jobPortalResponse.setData(response);
        jobPortalResponse.setMessage(messageSource.getMessage("count.application",null,Locale.getDefault()));
        return new ResponseEntity<>(jobPortalResponse,HttpStatus.OK);
    }
}
