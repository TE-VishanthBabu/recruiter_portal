package com.job_portal.recruiter_portal.repository;

import com.job_portal.recruiter_portal.entity.JobSeeker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobSeekerProfileRepository extends JpaRepository<JobSeeker,String> {
}
