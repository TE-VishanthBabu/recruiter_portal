package com.job_portal.recruiter_portal.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MeetingScheduleRequest {
    private String recruiterId;
    private String jobSeekerId;
    private String jobApplicationId;
    private MeetingRequest meetingRequest;
    private String location;
}