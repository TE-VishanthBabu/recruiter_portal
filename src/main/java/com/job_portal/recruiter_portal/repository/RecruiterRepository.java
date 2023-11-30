package com.job_portal.recruiter_portal.repository;

import com.job_portal.recruiter_portal.entity.Recruiter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecruiterRepository extends JpaRepository<Recruiter,String> {
}
