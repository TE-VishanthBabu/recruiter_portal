package com.job_portal.recruiter_portal.repository;

import com.job_portal.recruiter_portal.entity.JobPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface JobPostRepository extends JpaRepository<JobPost,String> {
    JobPost findByPostedDateNotNullAndPostStatusFalse();
    JobPost findByIdAndPostedDateNotNullAndPostStatusTrueAndExpirationDateAfterAndPostedStatusEquals(String jobPostId, Date currentDate,String status);
    JobPost findByIdAndPostStatusFalseAndPostedStatusEquals(String jobPostId,String status);
    Long countByPostedDateBetweenAndPostStatusTrueAndExpirationDateAfterAndPostedStatusEquals(Date startDate, Date endDate,Date currentDate,String status);
    List<JobPost> findByRecruiterId(String recruiterId);
    JobPost findByIdAndPostedDateNotNullAndPostStatusTrueAndPostedStatusEquals(String jobPostId,String status);
}
