package com.job_portal.recruiter_portal.controller;

import com.job_portal.recruiter_portal.entity.Recruiter;
import com.job_portal.recruiter_portal.request.RecruiterRequest;
import com.job_portal.recruiter_portal.response.JobPortalResponse;
import com.job_portal.recruiter_portal.service.RecruiterProfileService;
import com.job_portal.recruiter_portal.service.UploadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.Locale;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/recruiters")
public class RecruiterRegisterController {

    private final UploadService uploadService;
    private final RecruiterProfileService profileService;
    private final MessageSource messageSource;

    /**
     * Image upload.
     *
     * @param file  Multipart file
     * @return uploaded image response
     */
    @PostMapping("/upload/image")
    public ResponseEntity<JobPortalResponse> uploadImage(@RequestPart MultipartFile file) {
        String fileName = this.uploadService.uploadImage(file);
        log.info("Uploaded image filename: {}",fileName);
        JobPortalResponse response = new JobPortalResponse();
        response.setData(fileName);
        response.setMessage(messageSource.getMessage("image.upload",null, Locale.getDefault()));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Pdf upload.
     *
     * @param file  Multipart file
     * @return uploaded pdf response
     */
    @PostMapping("/upload/pdf")
    public ResponseEntity<JobPortalResponse> uploadPdf(@RequestPart MultipartFile file) {
        String fileName = this.uploadService.uploadPdf(file);
        log.info("Uploaded pdf filename: {}",fileName);
        JobPortalResponse response = new JobPortalResponse();
        response.setData(fileName);
        response.setMessage(messageSource.getMessage("pdf.upload",null,Locale.getDefault()));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/{recruiterId}")
    public ResponseEntity<JobPortalResponse> updateProfile(@PathVariable String recruiterId, @RequestBody RecruiterRequest recruiterRequest) {
        Recruiter recruiter = this.profileService.updateRecruiterProfile(recruiterId,recruiterRequest);
        JobPortalResponse response = new JobPortalResponse();
        response.setData(recruiter);
        response.setMessage(messageSource.getMessage("recruiter.profile",null,Locale.getDefault()));
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @GetMapping("/{recruiterId}")
    public ResponseEntity<JobPortalResponse> getProfile(@PathVariable String recruiterId) {
        JobPortalResponse response = new JobPortalResponse();
        response.setData(this.profileService.getRecruiterProfile(recruiterId));
        response.setMessage(messageSource.getMessage("get.recruiter",null,Locale.getDefault()));
        return new ResponseEntity<>(response,HttpStatus.OK);
    }
}
