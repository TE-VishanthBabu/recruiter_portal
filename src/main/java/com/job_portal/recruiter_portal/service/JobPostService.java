package com.job_portal.recruiter_portal.service;

import co.elastic.clients.elasticsearch.core.IndexResponse;
import com.job_portal.recruiter_portal.constants.Constant;
import com.job_portal.recruiter_portal.customException.CommonException;
import com.job_portal.recruiter_portal.entity.CompanyProfile;
import com.job_portal.recruiter_portal.entity.IndustryType;
import com.job_portal.recruiter_portal.entity.JobPost;
import com.job_portal.recruiter_portal.entity.Recruiter;
import com.job_portal.recruiter_portal.repository.CompanyProfileRepository;
import com.job_portal.recruiter_portal.repository.JobApplicationRepository;
import com.job_portal.recruiter_portal.repository.JobPostRepository;
import com.job_portal.recruiter_portal.repository.RecruiterRepository;
import com.job_portal.recruiter_portal.repository.IndustryTypeRepository;
import com.job_portal.recruiter_portal.request.JobPostRequest;
import com.job_portal.recruiter_portal.request.JobPostSearchRequest;
import com.job_portal.recruiter_portal.response.PostedJobsResponse;
import com.job_portal.recruiter_portal.utils.DateUtils;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.modelmapper.ModelMapper;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Comparator;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class JobPostService {

    private final JobPostRepository jobPostRepository;
    private final RecruiterRepository recruiterRepository;
    private final JobApplicationRepository jobApplicationRepository;
    private final CompanyProfileRepository profileRepository;
    private final IndustryTypeRepository industryTypeRepository;
    private final ModelMapper modelMapper;
    private final MessageSource messageSource;
    private final DateUtils dateUtils;
    private final ElasticSearchService elasticSearchService;

    /**
     * Creating job post by recruiter.
     *
     * @param recruiterId
     * @param jobPostRequest
     * @return Created job post.
     */
    public JobPost createJobPost(String recruiterId, JobPostRequest jobPostRequest) {
        Recruiter recruiter = recruiterRepository.findById(recruiterId).orElseThrow(() -> {
            throw new CommonException(messageSource.getMessage("recruiter.notFound", null, Locale.getDefault()), HttpStatus.NOT_FOUND);
        });
        JobPost jobPost = modelMapper.map(jobPostRequest, JobPost.class);
        if(jobPostRequest.getPostedDate()!=null) {
            if (jobPostRequest.getPostStatus().equals(true) && (dateUtils.getFormattedDate(jobPostRequest.getPostedDate()).equals(dateUtils.getFormattedDate(new Date())))) {
                jobPost.setPostedStatus(Constant.POSTED);
                jobPost.setPostStatus(true);
                LocalDate expiredDate = LocalDate.now().plusDays(15);
                jobPost.setExpirationDate(dateUtils.convertLocalDateToDate(expiredDate));

            } else if (jobPostRequest.getPostStatus().equals(true)) {
                jobPost.setPostStatus(false);
                jobPost.setPostedStatus(Constant.SCHEDULED);
                LocalDate expiredLocalDate = dateUtils.convertToLocalDateViaInstant(jobPostRequest.getPostedDate());
                LocalDate expiredDate = expiredLocalDate.plusDays(15);
                jobPost.setExpirationDate(dateUtils.convertLocalDateToDate(expiredDate));
            }
        }
        if(jobPostRequest.getPostStatus().equals(false)) {
            jobPost.setPostedStatus(Constant.DRAFT);
            jobPost.setPostStatus(false);
            jobPost.setExpirationDate(null);
        }
        CompanyProfile profile = profileRepository.findByRecruiterId(recruiterId);
        jobPost.setRecruiterId(recruiter.getId());
        jobPost.setCompanyPhoto(profile.getCompanyPhoto());
        jobPost.setCompanyName(recruiter.getCompanyName());
        if(ObjectUtils.isNotEmpty(jobPostRequest.getOthers())) {
            jobPost.setIndustryType(jobPostRequest.getOthers());
        }
        jobPost = this.jobPostRepository.save(jobPost);
        List<JobPost> jobPosts = recruiter.getJobPost();
        jobPosts.add(jobPost);
        recruiter.setJobPost(jobPosts);
        this.recruiterRepository.save(recruiter);
        IndexResponse response = this.elasticSearchService.createDocument(jobPost, "job_post");
        jobPost.setIndexId(response.id());
        return jobPostRepository.save(jobPost);
    }

    /**
     * Update job post.
     *
     * @param recruiterId
     * @param jobPostRequest
     * @return updated job post.
     */

    public JobPost updateJobPost(String recruiterId,JobPostRequest jobPostRequest){
        this.recruiterRepository.findById(recruiterId).orElseThrow(() -> {
            throw new CommonException(messageSource.getMessage("recruiter.notFound",null, Locale.getDefault()), HttpStatus.NOT_FOUND);
        });
        JobPost post = jobPostRepository.findById(jobPostRequest.getJobPostId()).orElseThrow(() -> {
            throw new CommonException(messageSource.getMessage("jobPost.notFound",null, Locale.getDefault()), HttpStatus.NOT_FOUND);
        });
        post.setPosition(jobPostRequest.getPosition());
        post.setOpenings(jobPostRequest.getOpenings());
        post.setLocation(jobPostRequest.getLocation());
        post.setNoticePeriod(jobPostRequest.getNoticePeriod());
        post.setExpirationDate(jobPostRequest.getExpirationDate());
        post.setPostedDate(jobPostRequest.getPostedDate());
        post.setMinExp(jobPostRequest.getMinExp());
        post.setMaxExp(jobPostRequest.getMaxExp());
        post.setMinSalary(jobPostRequest.getMinSalary());
        post.setMaxSalary(jobPostRequest.getMaxSalary());
        post.setQualifications(jobPostRequest.getQualifications());
        post.setResponsibilities(jobPostRequest.getResponsibilities());
        if(ObjectUtils.isNotEmpty(jobPostRequest.getOthers())) {
            post.setIndustryType(jobPostRequest.getOthers());
        }
        if(jobPostRequest.getPostedDate()!=null) {
            if ((post.getPostStatus().equals(false) && jobPostRequest.getPostStatus().equals(true)) && (dateUtils.getFormattedDate(jobPostRequest.getPostedDate()).equals(dateUtils.getFormattedDate(new Date())))) {
                post.setPostedStatus(Constant.POSTED);
                post.setPostStatus(true);
                LocalDate expiredDate = LocalDate.now().plusDays(15);
                post.setExpirationDate(dateUtils.convertLocalDateToDate(expiredDate));
                post = jobPostRepository.save(post);
                this.elasticSearchService.updateDocument(post, "job_post");
            } else if (jobPostRequest.getPostStatus().equals(true)) {
                post.setPostStatus(false);
                post.setPostedStatus(Constant.SCHEDULED);
                LocalDate expiredLocalDate = dateUtils.convertToLocalDateViaInstant(jobPostRequest.getPostedDate());
                LocalDate expiredDate = expiredLocalDate.plusDays(15);
                post.setExpirationDate(dateUtils.convertLocalDateToDate(expiredDate));
                post = jobPostRepository.save(post);
                this.elasticSearchService.updateDocument(post, "job_post");
            } else if (jobPostRequest.getPostStatus().equals(false)) {
                post.setPostedStatus(Constant.DRAFT);
                post.setPostStatus(false);
                post = jobPostRepository.save(post);
                this.elasticSearchService.updateDocument(post, "job_post");
            }
        } else if (jobPostRequest.getPostStatus().equals(false)) {
            post.setPostedStatus(Constant.DRAFT);
            post.setPostStatus(false);
            post = jobPostRepository.save(post);
            this.elasticSearchService.updateDocument(post, "job_post");
        }
        return post;
    }

    /**
     * Get all posted jobs by recruiter.
     *
     * @param recruiterId
     * @param status post status
     * @return posted jobs
     */
    public List<PostedJobsResponse> getAllPostedJobs(String recruiterId,String status) {
        Recruiter recruiter=this.recruiterRepository.findById(recruiterId).orElseThrow(() -> {
            throw new CommonException(messageSource.getMessage("recruiter.notFound",null, Locale.getDefault()), HttpStatus.NOT_FOUND);
        });
        List<PostedJobsResponse> jobsResponses = new ArrayList<>();
        if(status!=null ) {
            if (status.equals(Constant.POSTED)) {
                for (JobPost jobPost : recruiter.getJobPost()) {
                    JobPost post = jobPostRepository.findByIdAndPostedDateNotNullAndPostStatusTrueAndExpirationDateAfterAndPostedStatusEquals(jobPost.getId(),new Date(),status);
                    if (post != null) {
                        PostedJobsResponse postedJobsResponse = modelMapper.map(post, PostedJobsResponse.class);
                        jobsResponses.add(postedJobsResponse);
                    }
                }
            } else if (status.equals(Constant.DRAFT)) {
                for (JobPost jobPost : recruiter.getJobPost()) {
                    JobPost post = jobPostRepository.findByIdAndPostStatusFalseAndPostedStatusEquals(jobPost.getId(),status);
                    if (post != null) {
                        PostedJobsResponse postedJobsResponse = modelMapper.map(post, PostedJobsResponse.class);
                        jobsResponses.add(postedJobsResponse);
                    }
                }
            }
        }
        if(status.equals(Constant.POSTED)) {
            jobsResponses.sort(Comparator.comparing(PostedJobsResponse::getPostedDate).reversed());
        } else if (status.equals(Constant.DRAFT)) {
            jobsResponses.sort(Comparator.comparing(PostedJobsResponse::getCreationDate).reversed());
        }
        return jobsResponses;
    }

    /**
     * To view posted job by jobPostId.
     *
     * @param jobPostId
     * @return job post
     */
    public PostedJobsResponse viewJobPost(String jobPostId) {
        JobPost jobPost = jobPostRepository.findById(jobPostId).orElseThrow(() -> {
            throw new CommonException(messageSource.getMessage("jobPost.notFound",null, Locale.getDefault()), HttpStatus.NOT_FOUND);
        });
        PostedJobsResponse postedJobsResponse = modelMapper.map(jobPost,PostedJobsResponse.class);
        Long countByJobPostId = jobApplicationRepository.countByJobPostId(jobPost.getId());
        postedJobsResponse.setJobApplicationCount(countByJobPostId);
        return postedJobsResponse;
    }

    /**
     * Scheduler to post the jobs.
     * Runs on daily 12am
     */
    @Scheduled(cron = "0 0 0 * * *")
    public void scheduledJobPost() {
        JobPost post = this.jobPostRepository.findByPostedDateNotNullAndPostStatusFalse();
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate scheduledPostedDate = LocalDate.parse(new SimpleDateFormat("yyyy-MM-dd").
                format(post.getPostedDate()), dateFormat);
        LocalDate expiredDate = LocalDate.parse(new SimpleDateFormat("yyyy-MM-dd").
                format(post.getExpirationDate()), dateFormat);
        LocalDate currentDate = LocalDate.now();
        if((currentDate.equals(scheduledPostedDate))) {
            post.setPostStatus(true);
            post.setPostedStatus(Constant.POSTED);
            post = jobPostRepository.save(post);
            this.elasticSearchService.updateDocument(post, "job_post");
        }
        if(currentDate.equals(expiredDate)) {
            post.setDeleted(true);
            jobPostRepository.save(post);
        }
    }

    public void deleteIndex() {
        this.elasticSearchService.deleteIndex("job_post");
    }

    public Map<String,Object> searchJobPost(String recruiterId,JobPostSearchRequest jobPostSearchRequest) {
        return elasticSearchService.searchJobPosts(recruiterId,"job_post", jobPostSearchRequest);
    }

    public Map<String,Object> searchDraftJobPost(String recruiterId,JobPostSearchRequest jobPostSearchRequest) {
        return elasticSearchService.searchDraftJobPosts(recruiterId,"job_post",jobPostSearchRequest);
    }

    @SneakyThrows
    public List<String> suggestJobPosts(String keyword, String field) {
        return elasticSearchService.suggestJobPosts("job_post", keyword, field);
    }

    public List<IndustryType> getAllIndustries() {
        return industryTypeRepository.findAll(Sort.by(Sort.Direction.ASC, "creationDate"));
    }

    public List<IndustryType> addIndustry(List<IndustryType> types) {
        List<IndustryType> industryTypes = new ArrayList<>();
        for(IndustryType industryType:types) {
            IndustryType type = new IndustryType();
            type.setName(industryType.getName());
            type = industryTypeRepository.save(type);
            industryTypes.add(type);
        }
        return industryTypes;
    }
}
