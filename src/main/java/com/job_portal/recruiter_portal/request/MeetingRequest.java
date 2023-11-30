package com.job_portal.recruiter_portal.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class MeetingRequest {
    private String agenda;
    private Integer duration;
    private String password;
    private String scheduleFor;
    private Settings settings;
    private String start_time;
    private String timezone;
    private String topic;
    private Integer type;
}
