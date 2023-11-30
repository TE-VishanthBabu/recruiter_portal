package com.job_portal.recruiter_portal.entity;

import com.job_portal.recruiter_portal.entity.generic.AbstractEntity;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class IndustryType extends AbstractEntity {
    private String name;
}
