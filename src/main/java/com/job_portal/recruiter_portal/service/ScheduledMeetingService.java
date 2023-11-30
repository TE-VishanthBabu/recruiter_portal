package com.job_portal.recruiter_portal.service;

import com.job_portal.recruiter_portal.customException.CommonException;
import com.job_portal.recruiter_portal.entity.JobSeeker;
import com.job_portal.recruiter_portal.entity.MeetingSchedule;
import com.job_portal.recruiter_portal.entity.JobApplication;
import com.job_portal.recruiter_portal.entity.JobPost;
import com.job_portal.recruiter_portal.repository.JobSeekerProfileRepository;
import com.job_portal.recruiter_portal.repository.MeetingScheduleRepository;
import com.job_portal.recruiter_portal.repository.JobApplicationRepository;
import com.job_portal.recruiter_portal.repository.JobPostRepository;
import com.job_portal.recruiter_portal.request.MeetingScheduleRequest;
import com.job_portal.recruiter_portal.response.ScheduledMeetingResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.modelmapper.ModelMapper;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.time.LocalDate;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Comparator;
import java.util.Collections;

@Service
@Slf4j
@AllArgsConstructor
public class ScheduledMeetingService {
    private final MeetingScheduleRepository scheduleRepository;
    private final JobApplicationRepository jobApplicationRepository;
    private final JobPostRepository jobPostRepository;
    private final JobSeekerProfileRepository profileRepository;
    private final MessageSource messageSource;
    private final ZoomService zoomService;
    private final ModelMapper modelMapper;


    /**
     * Get all active scheduled meetings.
     *
     * @param recruiterId
     * @return list of active scheduled meeting responses
     */
    public List<ScheduledMeetingResponse> getActiveMeeting(String recruiterId,String status) {
        List<ScheduledMeetingResponse> meetings =null;
        if(ObjectUtils.isNotEmpty(status)){
            if(status.equals("Previous")) {
                List<MeetingSchedule> meetingSchedules = this.scheduleRepository.findAllByRecruiterIdAndStartTimeBefore(recruiterId, new Date());
                 meetings = getMeetings(meetingSchedules);
            }
        } else {
            List<MeetingSchedule> meetingSchedules = this.scheduleRepository.findAllByRecruiterIdAndStartTimeAfterAndInterviewStatusEquals(recruiterId,new Date(),"In-Progress");
            meetings = getMeetings(meetingSchedules);
        }
        Comparator<ScheduledMeetingResponse> scheduleComparator = (c1, c2) -> {
            return Long.valueOf(c2.getMeetingSchedule().getStartTime().getTime()).compareTo(c1.getMeetingSchedule().getStartTime().getTime());
        };
        Collections.sort(meetings,scheduleComparator);
        return meetings;
    }

    /**
     * To get the meeting details.
     *
     * @param meetingSchedules
     * @return scheduled meeting response
     */
    public List<ScheduledMeetingResponse> getMeetings(List<MeetingSchedule> meetingSchedules) {
        List<ScheduledMeetingResponse> meetingSchedulesList = new ArrayList<>();
        for(MeetingSchedule schedule:meetingSchedules) {
            ScheduledMeetingResponse meetingSchedule = new ScheduledMeetingResponse();
            meetingSchedule = modelMapper.map(schedule,ScheduledMeetingResponse.class);
            JobApplication jobApplication = this.jobApplicationRepository.findById(schedule.getJobApplicationId()).orElseThrow(() -> {
                throw new CommonException(messageSource.getMessage("job-application.notFound", null, Locale.getDefault()), HttpStatus.NOT_FOUND);
            });
            JobPost post = this.jobPostRepository.findById(jobApplication.getJobPostId()).orElseThrow(() -> {
                throw new CommonException(messageSource.getMessage("jobPost.notFound", null, Locale.getDefault()), HttpStatus.NOT_FOUND);
            });
            JobSeeker jobSeeker = this.profileRepository.findById(schedule.getJobSeekerId()).orElseThrow(()->{
                throw new CommonException(messageSource.getMessage("jobSeeker.notFound",null,Locale.getDefault()),HttpStatus.NOT_FOUND);
            });
            meetingSchedule.setUserPhoto(jobSeeker.getPhoto());
            meetingSchedule.setUserName(jobSeeker.getFirstName()+" "+jobSeeker.getLastName());
            meetingSchedule.setPosition(post.getPosition());
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String formattedInterviewDateTime = dateFormat.format(schedule.getStartTime());
            meetingSchedule.setInterviewDateTime(formattedInterviewDateTime);
            meetingSchedulesList.add(meetingSchedule);
        }
        return meetingSchedulesList;
    }

    /**
     * Update scheduled meeting.
     *
     * @param meetingId
     * @param request   Meeting schedule DTO
     * @return updated meeting schedule
     */
    public MeetingSchedule updateScheduledMeeting(String meetingId, MeetingScheduleRequest request) {
        MeetingSchedule schedule = this.scheduleRepository.findByMeetingId(meetingId);
        if(schedule!=null) {
            zoomService.updateMeeting(meetingId, request.getMeetingRequest());
            Map<String, Object> meeting = zoomService.getMeeting(meetingId);
            schedule = zoomService.saveMeeting(request, meeting);
            return schedule;
        } else {
            throw new CommonException("No Scheduled meeting Id found : "+meetingId,HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Delete scheduled meeting.
     *
     * @param meetingId
     */
    public void deleteScheduledMeeting(String meetingId) {
        MeetingSchedule schedule = this.scheduleRepository.findByMeetingId(meetingId);
        if(schedule!=null) {
            schedule.setDeleted(true);
            scheduleRepository.save(schedule);
            zoomService.deleteMeeting(meetingId);
        }
        log.info("Scheduled Meeting has been deleted in DB");
    }

    /**
     * Get schedule meeting details.
     *
     * @param meetingId
     * @return meetingSchedule
     */
    public ScheduledMeetingResponse getScheduledMeeting(String meetingId) {
        MeetingSchedule schedule = this.scheduleRepository.findByMeetingId(meetingId);
        ScheduledMeetingResponse response = new ScheduledMeetingResponse();
        response.setMeetingSchedule(schedule);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedInterviewDateTime = dateFormat.format(schedule.getStartTime());
        response.setInterviewDateTime(formattedInterviewDateTime);
        return response;
    }

    /**
     * Get meeting details by job-application id.
     *
     * @param jobApplicationId
     * @return meeting schedule
     */
    public ScheduledMeetingResponse getMeetingByJobApplicationId(String jobApplicationId) {
        MeetingSchedule schedule = this.scheduleRepository.findByJobApplicationId(jobApplicationId).orElseThrow(()->{
            throw new CommonException("Job-application Id not found",HttpStatus.NOT_FOUND);
        });
        ScheduledMeetingResponse response = new ScheduledMeetingResponse();
        response.setMeetingSchedule(schedule);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedInterviewDateTime = dateFormat.format(schedule.getStartTime());
        response.setInterviewDateTime(formattedInterviewDateTime);
        return response;
    }
}
