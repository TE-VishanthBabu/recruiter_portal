package com.job_portal.recruiter_portal.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostedJobsResponse {
    private String id;
    private String companyName;
    private String companyPhoto;
    private String title;
    private String position;
    private String overview;
    private String jobType;
    private String responsibilities;
    private String qualifications;
    private Integer openings;
    private Double minSalary;
    private Double maxSalary;
    private Integer minExp;
    private Integer maxExp;
    private List<String> location;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date postedDate;
    private String department;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date expirationDate;
    private String noticePeriod;
    private Boolean postStatus;
    private String industryType;
    private String keySkills;
    private String workType;
    private Long jobApplicationCount;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date creationDate;
}
