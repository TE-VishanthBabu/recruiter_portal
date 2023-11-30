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
@Table(name = "education")
@JsonIgnoreProperties(ignoreUnknown = true)
public class EducationInfo extends AbstractEntity {
    private String degree;
    private String institutionName;
    private String from;
    private String to;
    private String grade;
    private String location;
}
