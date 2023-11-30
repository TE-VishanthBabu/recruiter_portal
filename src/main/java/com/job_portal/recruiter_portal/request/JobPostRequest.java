package com.job_portal.recruiter_portal.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobPostRequest {
    @NotNull(message = "Position should not be null")
    @NotEmpty(message = "PLease enter position")
    private String position;
    @Column(length = 10000)
    @NotNull(message = "Responsibilities should not be null")
    @NotEmpty(message = "Please enter responsibilities")
    private String responsibilities;
    @NotNull(message = "Qualification should not be null")
    @NotEmpty(message = "PLease enter qualification")
    private String qualifications;
    @Column(name = "no_of_openings")
    private Integer openings;
    @NotNull(message = "Minimum salary should not be null")
    private Double minSalary;
    @NotNull(message = "Maximum salary should not be null")
    private Double maxSalary;
    @NotNull(message = "Minimum experience should not be null")
    private Integer minExp;
    @NotNull(message = "Maximum experience should not be null")
    private Integer maxExp;
    @ElementCollection
    private List<String> location;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date postedDate;
    private String department;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date expirationDate;
    private String noticePeriod;
    private Boolean postStatus;
    private String industryType;
    @NotNull(message = "Key skills should not be null")
    @NotEmpty(message = "Please enter Key skills")
    private String keySkills;
    @NotNull(message = "Work type should not be null")
    @NotEmpty(message = "Please enter work type")
    private String workType;
    private String jobType;
    private String jobPostId;
    private String others;
}

