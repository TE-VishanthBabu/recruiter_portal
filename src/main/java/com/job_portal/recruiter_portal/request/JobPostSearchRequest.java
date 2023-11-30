package com.job_portal.recruiter_portal.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobPostSearchRequest {
    private String keyword;
    private List<String> locations;
    private List<String> positions;
    private Integer size;
    private Integer from;
    private List<RangeRequest> experience;
    private List<RangeRequest> salary;
    private List<String> postedDates;
    private String recruiterId;
}
