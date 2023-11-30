package com.job_portal.recruiter_portal.response;

import com.job_portal.recruiter_portal.entity.JobApplication;
import com.job_portal.recruiter_portal.entity.JobSeeker;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobSeekerInfoResponse {
    private JobSeeker jobSeeker;
    private List<JobApplication> application;
    private String position;
    private String opening;
    private String title;
    private List<String> location;
    private String appliedDate;
}
