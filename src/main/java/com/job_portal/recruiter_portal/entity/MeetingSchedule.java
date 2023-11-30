package com.job_portal.recruiter_portal.entity;

import com.job_portal.recruiter_portal.entity.generic.AbstractEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Where(clause = "deleted=false")
public class MeetingSchedule extends AbstractEntity {

    private String meetingId;
    private String password;
    private String duration;
    private Date startTime;
    private String topic;
    private String agenda;
    private String jobSeekerId;
    private String recruiterId;
    private String jobApplicationId;
    @Column(length = 10000)
    private String meetingResponse;
    private String location;
    private String interviewStatus;
}

