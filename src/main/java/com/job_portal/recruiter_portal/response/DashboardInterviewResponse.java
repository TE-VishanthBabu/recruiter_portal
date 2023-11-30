package com.job_portal.recruiter_portal.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DashboardInterviewResponse {
    private String interviewCandidateName;
    private String interviewDateTime;
}
