package com.job_portal.recruiter_portal.service;

import com.job_portal.recruiter_portal.constants.Constant;
import com.job_portal.recruiter_portal.constants.HiringStage;
import com.job_portal.recruiter_portal.customException.CommonException;
import com.job_portal.recruiter_portal.entity.JobApplication;
import com.job_portal.recruiter_portal.entity.JobPost;
import com.job_portal.recruiter_portal.entity.JobSeeker;
import com.job_portal.recruiter_portal.entity.Recruiter;
import com.job_portal.recruiter_portal.entity.MeetingSchedule;
import com.job_portal.recruiter_portal.entity.Notes;
import com.job_portal.recruiter_portal.repository.JobApplicationRepository;
import com.job_portal.recruiter_portal.repository.JobPostRepository;
import com.job_portal.recruiter_portal.repository.JobSeekerProfileRepository;
import com.job_portal.recruiter_portal.repository.MeetingScheduleRepository;
import com.job_portal.recruiter_portal.repository.RecruiterRepository;
import com.job_portal.recruiter_portal.request.JobApplicationFilterRequest;
import com.job_portal.recruiter_portal.request.JobApplicationRequest;
import com.job_portal.recruiter_portal.response.JobApplicationResponse;
import com.job_portal.recruiter_portal.response.MyApplicationResponse;
import com.job_portal.recruiter_portal.response.DashboardInterviewResponse;
import com.job_portal.recruiter_portal.response.MyApplicantsResponse;
import com.job_portal.recruiter_portal.utils.DateUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.modelmapper.ModelMapper;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.text.SimpleDateFormat;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class JobApplicationService {

    private final JobApplicationRepository jobApplicationRepository;
    private final JobSeekerProfileRepository jobSeekerProfileRepository;
    private final MessageSource messageSource;
    private final JobPostRepository jobPostRepository;
    private final MeetingScheduleRepository scheduleRepository;
    private final ModelMapper modelMapper;
    private final RecruiterRepository recruiterRepository;

    /**
     * Get all job application by job post.
     *
     * @param jobPostId
     * @return Get all job application responses
     */
    public List<JobApplicationResponse> getAllJobApplicationByJobPost(String jobPostId, JobApplicationFilterRequest applicationFilterRequest) {
        List<JobApplicationResponse> applications = new ArrayList<>();
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM yyyy");
        List<JobApplication> jobApplications = this.jobApplicationRepository.findAllByJobPostId(jobPostId);
        List<List<JobApplication>> filteredLists = new ArrayList<>();
        filteredLists.add(new ArrayList<>(jobApplications));
        if(ObjectUtils.isNotEmpty(applicationFilterRequest.getAppliedDate())) {
        for(String appliedDateValue:applicationFilterRequest.getAppliedDate()) {
            List<JobApplication> filteredByDate = new ArrayList<>(filteredLists.get(filteredLists.size() - 1)); // Creating a copy of the last filtered list
            if(!appliedDateValue.equals("All")) {
                switch (appliedDateValue) {
                    case "Today":
                        filteredByDate = filteredByDate.stream().filter(application -> {
                            LocalDate appliedDate = LocalDate.parse(application.getAppliedDate(), formatter);
                            return appliedDate.equals(LocalDate.now());
                        }).collect(Collectors.toList());
                        break;
                    case "Yesterday":
                        LocalDate yesterday = today.minus(1, ChronoUnit.DAYS);
                        filteredByDate = filteredByDate.stream().filter(application -> {
                            LocalDate appliedDate = LocalDate.parse(application.getAppliedDate(), formatter);
                            return appliedDate.equals(yesterday);
                        }).collect(Collectors.toList());
                        break;
                    case "Last Week":
                        // Getting the end date of last week (previous Sunday)
                        LocalDate lastWeekEnd = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));

                        // Getting the start date of last week (previous Sunday - 6 days)
                        LocalDate lastWeekStart = lastWeekEnd.minusDays(6);
                        filteredByDate = filteredByDate.stream()
                                .filter(application -> {
                                    LocalDate appliedDate = LocalDate.parse(application.getAppliedDate(), formatter);
                                    return appliedDate.isAfter(lastWeekStart) && appliedDate.isBefore(lastWeekEnd.plusDays(1));
                                })
                                .collect(Collectors.toList());
                        break;

                    case "Last Month":
                        // Getting the end date of last month (end of the previous month)
                        LocalDate lastMonthEnd = today.with(TemporalAdjusters.firstDayOfMonth()).minusDays(1);

                        // Getting the start date of last month (first day of the previous month)
                        LocalDate lastMonthStart = lastMonthEnd.with(TemporalAdjusters.firstDayOfMonth());
                        filteredByDate = filteredByDate.stream()
                                .filter(application -> {
                                    LocalDate appliedDate = LocalDate.parse(application.getAppliedDate(), formatter);
                                    return appliedDate.isAfter(lastMonthStart) && appliedDate.isBefore(lastMonthEnd);
                                }).collect(Collectors.toList());
                        break;
                }
                }
            filteredLists.add(filteredByDate);
            }
        }
        if(ObjectUtils.isNotEmpty(applicationFilterRequest.getWorkExp())) {
            for(String workExp:applicationFilterRequest.getWorkExp()) {
                List<JobApplication> filteredByExperience = new ArrayList<>(filteredLists.get(filteredLists.size() - 1)); // Create a copy of the last filtered list
                switch (workExp) {
                    case "0 - 2 Years":
                        filteredByExperience = filteredByExperience.stream()
                                .filter(application -> {
                                    double totalExperience = Double.parseDouble(application.getTotalExperience());
                                    return totalExperience >= 0 && totalExperience <= 2;
                                })
                                .collect(Collectors.toList());
                        break;
                    case "2 - 4 Years":
                        filteredByExperience = filteredByExperience.stream()
                                .filter(application -> {
                                    double totalExperience = Double.parseDouble(application.getTotalExperience());
                                    return totalExperience >= 2 && totalExperience <= 4;
                                })
                                .collect(Collectors.toList());
                        break;
                    case "4 - 5 Years":
                        filteredByExperience = filteredByExperience.stream()
                                .filter(application -> {
                                    double totalExperience = Double.parseDouble(application.getTotalExperience());
                                    return totalExperience >= 4 && totalExperience <= 5;
                                })
                                .collect(Collectors.toList());
                        break;

                    case "5 Years & above":
                        filteredByExperience = filteredByExperience.stream()
                                .filter(application -> {
                                    double totalExperience = Double.parseDouble(application.getTotalExperience());
                                    return totalExperience >= 5;
                                })
                                .collect(Collectors.toList());
                        break;
                }
                filteredLists.add(filteredByExperience);
            }
        }
        List<JobApplication> finalFiltered = filteredLists.get(filteredLists.size() - 1);
        if(ObjectUtils.isNotEmpty(finalFiltered)) {
            for (JobApplication application : finalFiltered) {
                applications.add(this.mapJobApplicationResponse(application,jobPostId));
            }
        }
        return applications;
    }

    /**
     * Updating job application.
     *
     * @param jobApplicationId
     * @param request   Job-application DTO
     * @return updated job application
     */
    public JobApplication updateJobApplication(String jobApplicationId, JobApplicationRequest request) {
        JobApplication jobApplication = this.jobApplicationRepository.findById(jobApplicationId).orElseThrow(()->{
            throw new CommonException(messageSource.getMessage("job-application.notFound",null,Locale.getDefault()),HttpStatus.NOT_FOUND);
        });
        if(ObjectUtils.isNotEmpty(request.getNotes())) {
            jobApplication.setNotes(request.getNotes());
        }
        if(ObjectUtils.isNotEmpty(request.getStatus())) {
            switch (request.getStatus()) {
                case "in-review":
                    jobApplication.setHiringStatus(HiringStage.InReview.name());
                    log.info("Hiring stage moved to In-Review");
                    break;
                case "shortlisted":
                    jobApplication.setHiringStatus(HiringStage.Shortlisted.name());
                    jobApplication.setReviewedDate(new Date());
                    log.info("Hiring stage moved to shortlisted");
                    break;
                case "interview":
                    jobApplication.setHiringStatus(HiringStage.Interview.name());
                    log.info("Hiring stage moved to interview");
                    break;
                case "hired":
                    jobApplication.setHiringStatus(HiringStage.Hired.name());
                    log.info("Hiring stage moved to hired");
                    MeetingSchedule meetingSchedule = scheduleRepository.findByJobApplicationId(jobApplicationId).orElseThrow(() -> {
                        throw new CommonException("Schedule not found for job application", HttpStatus.NOT_FOUND);
                    });
                    meetingSchedule.setInterviewStatus("Completed");
                    scheduleRepository.save(meetingSchedule);
                    break;
                case "rejected":
                    jobApplication.setHiringStatus(HiringStage.Rejected.name());
                    log.info("Hiring stage moved to Rejected");
                    Optional<MeetingSchedule> schedule = scheduleRepository.findByJobApplicationId(jobApplicationId);
                    if(schedule.isPresent()) {
                        schedule.get().setInterviewStatus("Rejected");
                        scheduleRepository.save(schedule.get());
                    }
                    break;
                default:
                    log.info("No Hiring stages found");
            }
        }
        return jobApplicationRepository.save(jobApplication);
    }

    /**
     * Mapping all job applications.
     *
     * @param application
     * @return job application response
     */
    public JobApplicationResponse mapJobApplicationResponse(JobApplication application,String jobPostId){
        JobApplicationResponse applicationResponse = modelMapper.map(application,JobApplicationResponse.class);
        JobSeeker jobSeeker = this.jobSeekerProfileRepository.findById(application.getJobSeekerId()).orElseThrow(() -> {
            throw new CommonException(messageSource.getMessage("jobSeeker.notFound", null, Locale.getDefault()), HttpStatus.NOT_FOUND);
        });
        applicationResponse.setUserPhoto(jobSeeker.getPhoto());
        applicationResponse.setUserName(jobSeeker.getFirstName() + " " +jobSeeker.getLastName());
        JobPost jobPost = this.jobPostRepository.findById(jobPostId).orElseThrow(() -> {
            throw new CommonException(messageSource.getMessage("jobPost.notFound", null, Locale.getDefault()), HttpStatus.NOT_FOUND);
        });
        applicationResponse.setPosition(jobPost.getPosition());
        return applicationResponse;
    }

    /**
     * Delete job application
     *
     * @param jobApplicationId
     */
    public void deleteJobApplication(String jobApplicationId) {
        JobApplication application = this.jobApplicationRepository.findById(jobApplicationId).orElseThrow(()->{
            throw new CommonException("JobApplication id not found",HttpStatus.NOT_FOUND);
        });
        application.setDeleted(true);
        jobApplicationRepository.save(application);
        log.info("Job-application details deleted for the id: {}",jobApplicationId);
    }

    /**
     * Get job-application details.
     *
     * @param jobApplicationId
     * @return jobApplication
     */
    public JobApplication getJobApplicationById(String jobApplicationId) {
        JobApplication application = this.jobApplicationRepository.findById(jobApplicationId).orElseThrow(()->{
            throw new CommonException("JobApplication id not found",HttpStatus.NOT_FOUND);
        });
        if(ObjectUtils.isEmpty(application.getHiringStatus())) {
            application.setHiringStatus(HiringStage.InReview.name());
        }
        application = jobApplicationRepository.save(application);
        Comparator<Notes> applicationComparator = (c1, c2) -> {
            return Long.valueOf(c2.getMessageDateTime().getTime()).compareTo(c1.getMessageDateTime().getTime());
        };
        Collections.sort(application.getNotes(),applicationComparator);
        return application;
    }

    public List<MyApplicationResponse> getAllJobApplication(Date startDate,Date endDate, String recruiterId) {
        List<JobApplication> applications = null;
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -7);
        Date pastWeekDate = cal.getTime();
        if(startDate!=null && endDate!=null){
            applications = this.jobApplicationRepository.findAllByCreationDateBetween(pastWeekDate,new Date());
        } else {
            applications = this.jobApplicationRepository.findAllByOrderByCreationDateDesc();
        }
        List<MyApplicationResponse> applicationResponses = new ArrayList<>();
        for(JobApplication application:applications) {
            if(application.getRecruiterId()!=null && application.getRecruiterId().equals(recruiterId)) {
                MyApplicationResponse response = new MyApplicationResponse();
                JobPost post = jobPostRepository.findById(application.getJobPostId()).orElseThrow(() -> {
                    throw new CommonException("JobPost Id not found", HttpStatus.NOT_FOUND);
                });
                JobSeeker seeker = jobSeekerProfileRepository.findById(application.getJobSeekerId()).orElseThrow(()->{
                    throw new CommonException("JobSeeker id not found");
                });
                response.setWorkType(post.getWorkType());
                response.setCompanyPhoto(post.getCompanyPhoto());
                response.setJobLocation(post.getLocation());
                response.setPosition(post.getPosition());
                response.setJobSeekerLocation(application.getLocation());
                response.setAppliedDate(application.getAppliedDate());
                response.setHiringStatus(application.getHiringStatus());
                response.setJobApplicationId(application.getId());
                response.setJobPostId(application.getJobPostId());
                response.setJobSeekerName(seeker.getFirstName()+" "+seeker.getLastName());
                applicationResponses.add(response);
            }
        }
        return applicationResponses;
    }

    public MyApplicationResponse getApplicationCount(Date startDate,Date endDate,String recruiterId) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(endDate);
        calendar.add(Calendar.DATE,+1);
        Date lastDate = calendar.getTime();
       Long jobApplicationCount = this.jobApplicationRepository.countByCreationDateBetween(startDate,lastDate);
       Long jobPostCount = this.jobPostRepository.countByPostedDateBetweenAndPostStatusTrueAndExpirationDateAfterAndPostedStatusEquals(startDate,lastDate, new Date(), Constant.POSTED);
       Long selectedCandidates = this.jobApplicationRepository.countByLastModificationDateBetweenAndHiringStatusEquals(startDate,lastDate,HiringStage.Hired.name());
       Calendar cal = Calendar.getInstance();
       cal.add(Calendar.DATE, -7);
       Date pastWeekDate = cal.getTime();
       Long recentApplication = this.jobApplicationRepository.countByCreationDateBetween(pastWeekDate,new Date());
       List<MeetingSchedule> schedule = this.scheduleRepository.findAllByRecruiterIdAndStartTimeAfterAndInterviewStatusEquals(recruiterId,new Date(),"In-Progress");
       MyApplicationResponse response = new MyApplicationResponse();
       List<DashboardInterviewResponse> interviewResponses = new ArrayList<>();
        for(MeetingSchedule meetingSchedule:schedule) {
            DashboardInterviewResponse dashboardInterviewResponse = new DashboardInterviewResponse();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String currentMeetingDate = dateFormat.format(meetingSchedule.getStartTime());
            String currentDate = dateFormat.format(new Date());
            if(currentMeetingDate.equals(currentDate)) {
                JobSeeker seeker = jobSeekerProfileRepository.findById(meetingSchedule.getJobSeekerId()).orElseThrow(()->{
                    throw new CommonException("Job-seeker id not found");
                });
                dashboardInterviewResponse.setInterviewCandidateName(seeker.getFirstName()+" "+seeker.getLastName());
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String formattedInterviewDateTime = simpleDateFormat.format(meetingSchedule.getStartTime());
                dashboardInterviewResponse.setInterviewDateTime(formattedInterviewDateTime);
                interviewResponses.add(dashboardInterviewResponse);
            }
        }
        response.setInterviewResponses(interviewResponses);
        response.setCountTotalApplication(jobApplicationCount);
        response.setRecentApplicationCount(recentApplication);
        response.setActivePostCount(jobPostCount);
        response.setSelectedCandidatesCount(selectedCandidates);
        return response;
    }

    public Set<String> getAllPositions(String recruiterId) {
       List<JobPost> jobPosts = jobPostRepository.findByRecruiterId(recruiterId);
       List<String> positions = new ArrayList<>();
      for(JobPost jobPost:jobPosts) {
          positions.add(jobPost.getPosition());
      }
      return this.removeDuplicatesAndMerge(positions);
    }
    public Set<String> removeDuplicatesAndMerge(List<String> positions) {
        List<String> mergedList = positions.stream()
                .map(String::toUpperCase)
                .collect(Collectors.toList());
        Set<String> uniqueSet = new HashSet<>(mergedList); // Remove duplicates using a Set
        return uniqueSet;
    }

    public List<MyApplicantsResponse> getMyApplicants(String recruiterId) {
        Recruiter recruiter=this.recruiterRepository.findById(recruiterId).orElseThrow(() -> {
            throw new CommonException(messageSource.getMessage("recruiter.notFound",null, Locale.getDefault()), HttpStatus.NOT_FOUND);
        });
        List<MyApplicantsResponse> applicantsResponses = new ArrayList<>();
        for (JobPost jobPost : recruiter.getJobPost()) {
            JobPost post = jobPostRepository.findByIdAndPostedDateNotNullAndPostStatusTrueAndPostedStatusEquals(jobPost.getId(),Constant.POSTED);
            if(ObjectUtils.isNotEmpty(post)) {
                MyApplicantsResponse applicantsResponse = new MyApplicantsResponse();
                applicantsResponse.setId(post.getId());
                applicantsResponse.setPosition(post.getPosition());
                applicantsResponse.setOpenings(post.getOpenings());
                applicantsResponse.setPostedDate(post.getPostedDate());
                Long countByJobPostId = jobApplicationRepository.countByJobPostId(post.getId());
                applicantsResponse.setJobApplicationCount(countByJobPostId);
                applicantsResponses.add(applicantsResponse);
            }
        }
        if(ObjectUtils.isNotEmpty(applicantsResponses)) {
            applicantsResponses.sort(Comparator.comparing(MyApplicantsResponse::getPostedDate).reversed());
        }
        return applicantsResponses;
    }

}
