package com.job_portal.recruiter_portal.entity;

import com.job_portal.recruiter_portal.entity.generic.AbstractEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Where(clause = "deleted=false")
public class Recruiter extends AbstractEntity {

    private String userName;
    private String companyName;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    @Column(name = "official_id_proof")
    private String idProof;
    private String addressProof;
    private String phoneNumber;
    private String addressLine1;
    private String addressLine2;
    private String country;
    private String state;
    private String city;
    private String postalCode;
    private Boolean active;
    @OneToMany(cascade = CascadeType.ALL)
    private List<JobPost> jobPost = new ArrayList<>();
    private String verifiedBadge;
}
