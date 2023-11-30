package com.job_portal.recruiter_portal.response;

import com.job_portal.recruiter_portal.entity.JobApplication;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobApplicationResponse {
    private JobApplication application;
    private String userName;
    private String position;
    private String userPhoto;
}
