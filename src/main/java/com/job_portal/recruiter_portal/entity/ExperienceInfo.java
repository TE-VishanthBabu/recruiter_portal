package com.job_portal.recruiter_portal.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.job_portal.recruiter_portal.entity.generic.AbstractEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "experience")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExperienceInfo extends AbstractEntity {
    private String position;
    private String companyName;
    private String from;
    private String to;
    private String jobDescription;
}
