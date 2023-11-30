package com.job_portal.recruiter_portal.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.job_portal.recruiter_portal.entity.generic.UUIDEntity;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Notes extends UUIDEntity {
    private String message;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date messageDateTime;
}
