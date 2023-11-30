package com.job_portal.recruiter_portal.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyApplicationResponse {
    private String position;
    private String jobSeekerLocation;
    private String jobSeekerName;
    private String appliedDate;
    private String status;
    private String hiringStatus;
    private Long countTotalApplication;
    private List<String> jobLocation;
    private String workType;
    private String companyPhoto;
    private Long activePostCount;
    private Long recentApplicationCount;
    private Long selectedCandidatesCount;
    private String jobApplicationId;
    private String jobPostId;
    private List<DashboardInterviewResponse> interviewResponses;
}
