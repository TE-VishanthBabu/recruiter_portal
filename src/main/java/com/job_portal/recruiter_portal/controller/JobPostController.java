package com.job_portal.recruiter_portal.controller;

import com.job_portal.recruiter_portal.entity.IndustryType;
import com.job_portal.recruiter_portal.entity.JobPost;
import com.job_portal.recruiter_portal.request.JobApplicationFilterRequest;
import com.job_portal.recruiter_portal.request.JobPostRequest;
import com.job_portal.recruiter_portal.request.JobPostSearchRequest;
import com.job_portal.recruiter_portal.response.JobApplicationResponse;
import com.job_portal.recruiter_portal.response.JobPortalResponse;
import com.job_portal.recruiter_portal.response.PostedJobsResponse;
import com.job_portal.recruiter_portal.service.JobApplicationService;
import com.job_portal.recruiter_portal.service.JobPostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/recruiters")
public class JobPostController {

    private final JobPostService jobPostService;
    private final MessageSource messageSource;
    private final JobApplicationService applicationService;

    /**
     * Creating Job post by recruiter.
     *
     * @param recruiterId
     * @param jobPostRequest    Job-post DTO
     * @return created job post
     */
    @PostMapping("/{recruiterId}/job-posts")
    public ResponseEntity<JobPortalResponse> createJobPost(@PathVariable final String recruiterId, @Valid @RequestBody JobPostRequest jobPostRequest) {
        JobPost jobPost = this.jobPostService.createJobPost(recruiterId,jobPostRequest);
        log.info("New job post has been created by : {}",jobPost.getId());
        JobPortalResponse response = new JobPortalResponse();
        response.setData(jobPost);
        response.setMessage(messageSource.getMessage("jobPost.add",null, Locale.getDefault()));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * To update the job post.
     *
     * @param recruiterId
     * @param jobPostRequest
     * @return updated job post
     */
    @PutMapping("/{recruiterId}/job-posts")
    public ResponseEntity<JobPortalResponse> updateJobPost(@PathVariable final String recruiterId,
                                                           @RequestBody JobPostRequest jobPostRequest){
        JobPost post = this.jobPostService.updateJobPost(recruiterId,jobPostRequest);
        log.info("Updated successfully for the job-post id: {}",post.getId());
        JobPortalResponse response = new JobPortalResponse();
        response.setData(post);
        response.setMessage(messageSource.getMessage("jobPost.update",null,Locale.getDefault()));
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    /**
     * Get all posted jobs by the respective recruiter.
     *
     * @param recruiterId
     * @param status
     * @return list of posted jobs.
     */
    @GetMapping("/{recruiterId}/job-posts")
    public ResponseEntity<JobPortalResponse> getAllPostedJobs(@PathVariable final String recruiterId,@RequestParam(required = false) String status) {
        List<PostedJobsResponse> jobPosts= this.jobPostService.getAllPostedJobs(recruiterId,status);
        log.info("Gathered all jobs posted by recruiter id: {}",recruiterId);
        JobPortalResponse response = new JobPortalResponse();
        response.setData(jobPosts);
        response.setMessage(messageSource.getMessage("gathered.jobPosts",null,Locale.getDefault()));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * To view the job post details.
     *
     * @param jobPostId
     * @return job post
     */
    @GetMapping("/job-posts/{jobPostId}")
    public ResponseEntity<JobPortalResponse> getPostedJob(@PathVariable String jobPostId){
        PostedJobsResponse jobPost = this.jobPostService.viewJobPost(jobPostId);
        log.info("Gathered information of  Job post for the id : {}",jobPostId);
        JobPortalResponse response = new JobPortalResponse();
        response.setData(jobPost);
        response.setMessage(messageSource.getMessage("get.jobPost",null,Locale.getDefault()));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * To gather all job applications
     *
     * @param jobPostId
     * @return Getting all job applications responses for particular job post
     */
    @GetMapping("/job-posts/{jobPostId}/job-applications")
    public ResponseEntity<JobPortalResponse> getAllJobApplicationForParticularJobPost(@PathVariable final String jobPostId, @RequestBody(required = false)JobApplicationFilterRequest applicationFilterRequest) {
        List<JobApplicationResponse> jobApplicationResponses = this.applicationService.getAllJobApplicationByJobPost(jobPostId,applicationFilterRequest);
        log.info("Gathered all Job-applications");
        JobPortalResponse response = new JobPortalResponse();
        response.setData(jobApplicationResponses);
        response.setMessage(messageSource.getMessage("gathered.jobApplication",null, Locale.getDefault()));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     *
     * Scheduled posting job.
     */
    @GetMapping("/cron-job")
    public ResponseEntity<JobPortalResponse> schedulerJobPost() {
        this.jobPostService.scheduledJobPost();
        log.info("Scheduled job posting completed on: {}",new Date());
        JobPortalResponse response = new JobPortalResponse();
        response.setMessage(messageSource.getMessage("scheduled.jobPost",null,Locale.getDefault()));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/index")
    public void deleteIndex(){
        this.jobPostService.deleteIndex();
    }

    @PostMapping("/search/{recruiterId}")
    public ResponseEntity<JobPortalResponse> searchJobPost(@PathVariable String recruiterId,@RequestBody JobPostSearchRequest jobPostSearchRequest) {
        Map<String, Object> jobPosts = this.jobPostService.searchJobPost(recruiterId,jobPostSearchRequest);
        JobPortalResponse jobPortalResponse = new JobPortalResponse();
        jobPortalResponse.setData(jobPosts);
        return new ResponseEntity<>(jobPortalResponse, HttpStatus.OK);
    }

    @PostMapping("/search/draft-jobs/{recruiterId}")
    public ResponseEntity<JobPortalResponse> searchDraftJobs(@PathVariable String recruiterId, @RequestBody JobPostSearchRequest searchRequest) {
        Map<String, Object> jobPosts = this.jobPostService.searchDraftJobPost(recruiterId,searchRequest);
        JobPortalResponse jobPortalResponse = new JobPortalResponse();
        jobPortalResponse.setData(jobPosts);
        return new ResponseEntity<>(jobPortalResponse, HttpStatus.OK);
    }

    @GetMapping("/suggest")
    public ResponseEntity<JobPortalResponse> suggestJobPost(@RequestParam String keyword, @RequestParam String field) {
        List<String> jobPosts = this.jobPostService.suggestJobPosts(keyword, field);
        JobPortalResponse jobPortalResponse = new JobPortalResponse();
        jobPortalResponse.setData(jobPosts);
        return new ResponseEntity<>(jobPortalResponse, HttpStatus.OK);
    }

    /**
     * Get all industry type.
     *
     * @return list of Industry type
     */
    @GetMapping("/industries")
    public ResponseEntity<JobPortalResponse> getAllIndustries() {
        List<IndustryType> industries = this.jobPostService.getAllIndustries();
        JobPortalResponse jobPortalResponse = new JobPortalResponse();
        jobPortalResponse.setData(industries);
        jobPortalResponse.setMessage(messageSource.getMessage("getAll.industryType",null,Locale.getDefault()));
        return new ResponseEntity<>(jobPortalResponse, HttpStatus.OK);
    }

    @PostMapping("/industries")
    public ResponseEntity<JobPortalResponse> addIndustry(@RequestBody List<IndustryType> type) {
        JobPortalResponse jobPortalResponse = new JobPortalResponse();
        jobPortalResponse.setData( this.jobPostService.addIndustry(type));
        return new ResponseEntity<>(jobPortalResponse, HttpStatus.OK);
    }
}
