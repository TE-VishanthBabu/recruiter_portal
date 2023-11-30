package com.job_portal.recruiter_portal.repository;

import com.job_portal.recruiter_portal.entity.JobApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface JobApplicationRepository extends JpaRepository<JobApplication,String> {
    List<JobApplication> findAllByJobPostId(String jobPostId);
    List<JobApplication> findAllByJobSeekerId(String jobSeekerId);
    Long countByJobPostId(String jobPostId);

    List<JobApplication> findAllByCreationDateBetween(Date startDate,Date endDate);

    List<JobApplication> findAllByOrderByCreationDateDesc();

    Long countByCreationDateBetween(Date startDate,Date endDate);

    Long countByLastModificationDateBetweenAndHiringStatusEquals(Date startDate, Date endDate, String stage);

}
