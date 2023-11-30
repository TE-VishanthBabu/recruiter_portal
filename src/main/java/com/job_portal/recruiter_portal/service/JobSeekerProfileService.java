package com.job_portal.recruiter_portal.service;

import com.job_portal.recruiter_portal.entity.JobApplication;
import com.job_portal.recruiter_portal.entity.JobSeeker;
import com.job_portal.recruiter_portal.repository.JobApplicationRepository;
import com.job_portal.recruiter_portal.repository.JobPostRepository;
import com.job_portal.recruiter_portal.repository.JobSeekerProfileRepository;
import com.job_portal.recruiter_portal.request.JobSeekerSearchRequest;
import com.job_portal.recruiter_portal.response.JobSeekerInfoResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class JobSeekerProfileService {

    private final JobSeekerProfileRepository jobSeekerProfileRepository;
    private final JobApplicationRepository applicationRepository;
    private final ElasticSearchService elasticSearchService;
    private final JobPostRepository jobPostRepository;

    /**
     * Get jobSeeker profile.
     *
     * @param jobSeekerId
     * @return jobSeeker
     */
    public JobSeekerInfoResponse getJobSeekerInfoByJobSeekerId(String jobSeekerId) {
        Optional<JobSeeker> jobSeeker = this.jobSeekerProfileRepository.findById(jobSeekerId);
        if(jobSeeker.isPresent()){
            JobSeekerInfoResponse jobSeekerInfoResponse = new JobSeekerInfoResponse();
            jobSeekerInfoResponse.setJobSeeker(jobSeeker.get());
            List<JobApplication> jobApplications = this.applicationRepository.findAllByJobSeekerId(jobSeekerId);
            return jobSeekerInfoResponse;
        }
        return null;
    }

    public List<JobSeeker> getAllJobSeeker() {
        return this.jobSeekerProfileRepository.findAll();
    }

    /**
     * Search Job-seeker
     * @param searchRequest
     * @return
     */
    public Map<String,Object> searchJobSeeker(JobSeekerSearchRequest searchRequest) {
       return elasticSearchService.searchJobSeeker("job_seeker",searchRequest);
    }

    @SneakyThrows
    public List<String> suggestJobSeeker(String keyword, String field) {
        return elasticSearchService.suggestJobSeeker("job_seeker", keyword, field);
    }
}
