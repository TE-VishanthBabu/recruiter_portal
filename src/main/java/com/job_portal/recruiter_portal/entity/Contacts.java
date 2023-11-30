package com.job_portal.recruiter_portal.entity;

import com.job_portal.recruiter_portal.entity.generic.UUIDEntity;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Contacts extends UUIDEntity {
    private String email;
    private String phoneNumber;
    private String facebookLink;
    private String twitterLink;
    private String instaLink;
    private String linkedinLink;
}
