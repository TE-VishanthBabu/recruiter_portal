package com.job_portal.recruiter_portal.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.job_portal.recruiter_portal.entity.generic.AbstractEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.FetchType;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
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
public class CompanyProfile extends AbstractEntity {
    private String url;
    private String companyPhoto;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date founded;
    private String employee;
    private Integer location;
    private String industryType;
    @Column(length = 10000)
    private String companyProfileInfo;
    @ElementCollection
    private List<String> techStack;
    @ElementCollection
    private List<String> officeLocation;
    @OneToOne(cascade = CascadeType.ALL)
    private Contacts contact;
    @OneToOne(cascade = CascadeType.ALL)
    private Recruiter recruiter;
    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private List<JobPost> jobPosts;
}
