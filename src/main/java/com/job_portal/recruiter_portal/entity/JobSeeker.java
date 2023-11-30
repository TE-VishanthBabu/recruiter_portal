package com.job_portal.recruiter_portal.entity;

import com.job_portal.recruiter_portal.constants.VerifiedBadge;
import com.job_portal.recruiter_portal.entity.generic.AbstractEntity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Entity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;
import jakarta.persistence.Column;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.OneToOne;

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
public class JobSeeker extends AbstractEntity {

    private String userId;
    private String title;
    private String summary;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String gender;
    @Column(name = "dob")
    private Date dateOfBirth;
    private String addressLine1;
    private String addressLine2;
    private String country;
    private String state;
    private String city;
    private String postalCode;
    @ElementCollection
    private List<String> preferredJobLocation;
    private String resume;
    private String photo;
    private String idProof;
    private String addressProof;
    private String educationProof;
    private String experienceProof;
    private String status;
    private String totalExperience;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VerifiedBadge verifiedBadge;

    private boolean active = Boolean.TRUE;

    @OneToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    private List<EducationInfo> education;

    @OneToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    private List<ExperienceInfo> experience;

    @OneToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private JobSeekerVerificationBadge jobSeekerVerificationBadge;
    private String linkedIn;
    private String twitter;
    private String website;
    private String indexId;
}
