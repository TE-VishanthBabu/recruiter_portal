package com.job_portal.recruiter_portal.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.job_portal.recruiter_portal.entity.Contacts;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompanyProfileRequest {
    @NotNull(message = "Url should not be null")
    @NotEmpty(message = "Url should not be empty")
    private String url;
    private String companyPhoto;
    @NotNull(message = "Enter founded date should not be null")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date founded;
    @NotEmpty(message = "Employee should not be empty")
    @NotNull(message = "Employee should not be null")
    private String employee;
    @NotNull(message = "Location should not be null")
    private Integer location;
    @NotNull(message = "IndustryType should not be null")
    @NotEmpty(message = "IndustryType should not be empty")
    private String industryType;
    @NotEmpty(message = "Company profile should not be empty")
    @NotNull(message = "Company profile should not be null")
    private String companyProfileInfo;
    private List<String> techStack;
    private List<String> officeLocation;
    private Contacts contact;
    private String companyName;
}
