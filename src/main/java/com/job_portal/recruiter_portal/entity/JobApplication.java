package com.job_portal.recruiter_portal.entity;

import com.job_portal.recruiter_portal.entity.generic.AbstractEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
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
public class JobApplication extends AbstractEntity {
    private String resume;
    private String totalExperience;
    private String email;
    private String jobSeekerId;
    private String jobPostId;
    private String recruiterId;
    private String location;
    private String appliedDate;
    private Date reviewedDate;
    private String hiringStatus;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Notes> notes;
}
