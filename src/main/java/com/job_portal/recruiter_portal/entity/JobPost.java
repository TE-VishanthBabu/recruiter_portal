package com.job_portal.recruiter_portal.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.job_portal.recruiter_portal.entity.generic.AbstractEntity;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Where(clause = "deleted=false")
public class JobPost extends AbstractEntity {
    private String companyName;
    private String companyPhoto;
    private String title;
    private String position;
    private String overview;
    @Column(length = 10000)
    private String responsibilities;
    private String qualifications;
    @Column(name = "no_of_openings")
    private Integer openings;
    private Double minSalary;
    private Double maxSalary;
    private Integer minExp;
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
    private String keySkills;
    private String workType;
    private String jobType;
    private String postedStatus;
    private String recruiterId;
    private String indexId;
}

