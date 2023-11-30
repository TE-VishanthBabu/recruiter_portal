package com.job_portal.recruiter_portal.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyApplicantsResponse {
    private String id;
    private String position;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date postedDate;
    private Integer openings;
    private Long jobApplicationCount;
}
