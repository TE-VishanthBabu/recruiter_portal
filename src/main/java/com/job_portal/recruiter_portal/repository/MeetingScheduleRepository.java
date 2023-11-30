package com.job_portal.recruiter_portal.repository;

import com.job_portal.recruiter_portal.entity.MeetingSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface MeetingScheduleRepository extends JpaRepository<MeetingSchedule,String> {
    Optional<MeetingSchedule> findByJobApplicationId(String jobApplicationId);
    List<MeetingSchedule> findAllByRecruiterIdAndStartTimeAfterAndInterviewStatusEquals(String recruiterId,
                                                                                        Date currentDate,
                                                                                        String interviewStatus);
    List<MeetingSchedule> findAllByRecruiterIdAndStartTimeBefore(String recruiterId,Date currentDate);
    MeetingSchedule findByMeetingId(String meetingId);
}
