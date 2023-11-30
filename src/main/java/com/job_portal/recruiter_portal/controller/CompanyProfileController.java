package com.job_portal.recruiter_portal.controller;

import com.job_portal.recruiter_portal.entity.CompanyProfile;
import com.job_portal.recruiter_portal.request.CompanyProfileRequest;
import com.job_portal.recruiter_portal.response.JobPortalResponse;
import com.job_portal.recruiter_portal.service.CompanyProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/recruiters")
public class CompanyProfileController {

    private final CompanyProfileService profileService;
    private final MessageSource messageSource;

    /**
     * Create company profile.
     *
     * @param recruiterId
     * @param profileRequest    Company profile DTO
     * @return company profile response
     */
    @PostMapping("/{recruiterId}/company-profile")
    public ResponseEntity<JobPortalResponse> createCompanyProfile(@PathVariable String recruiterId, @Valid @RequestBody CompanyProfileRequest profileRequest) {
        CompanyProfile profile = this.profileService.createCompanyProfile(recruiterId,profileRequest);
        log.info("Company profile details added for the id: {}",profile.getId());
        JobPortalResponse response = new JobPortalResponse();
        response.setData(profile);
        response.setMessage(messageSource.getMessage("company.profile",null, Locale.getDefault()));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Get company profile details.
     *
     * @param recruiterId
     * @return company profile response
     */
    @GetMapping("/{recruiterId}/company-profile")
    public ResponseEntity<JobPortalResponse> getCompanyProfile(@PathVariable String recruiterId){
        CompanyProfile profile = this.profileService.getCompanyProfile(recruiterId);
        log.info("Gathered company profile details for the id: {}",profile.getId());
        JobPortalResponse response = new JobPortalResponse();
        response.setData(profile);
        response.setMessage(messageSource.getMessage("get.profile",null, Locale.getDefault()));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
