package com.job_portal.recruiter_portal.response;

import com.job_portal.recruiter_portal.entity.MeetingSchedule;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScheduledMeetingResponse {
    private MeetingSchedule meetingSchedule;
    private String position;
    private String jobApplicationId;
    private String userName;
    private String userPhoto;
    private String interviewDateTime;
}
