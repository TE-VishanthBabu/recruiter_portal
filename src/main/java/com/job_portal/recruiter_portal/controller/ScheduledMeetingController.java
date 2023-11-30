package com.job_portal.recruiter_portal.controller;

import com.job_portal.recruiter_portal.entity.MeetingSchedule;
import com.job_portal.recruiter_portal.request.MeetingScheduleRequest;
import com.job_portal.recruiter_portal.response.AuthResponse;
import com.job_portal.recruiter_portal.response.JobPortalResponse;
import com.job_portal.recruiter_portal.response.ScheduledMeetingResponse;
import com.job_portal.recruiter_portal.service.ScheduledMeetingService;
import com.job_portal.recruiter_portal.service.ZoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;
import java.util.Map;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/recruiters")
public class ScheduledMeetingController {

    private final ScheduledMeetingService meetingService;
    private final ZoomService zoomService;
    private final MessageSource messageSource;


    /**
     * Get scheduled active meetings.
     *
     * @param recruiterId
     * @return list of active meeting response.
     */
    @GetMapping("/{recruiterId}/scheduled-meetings")
    public ResponseEntity<JobPortalResponse> getActiveScheduledMeeting(@PathVariable String recruiterId,
                                                                       @RequestParam(required = false,name = "meetings") String status) {
        List<ScheduledMeetingResponse> activeMeeting= this.meetingService.getActiveMeeting(recruiterId,status);
        log.info("Gathered active scheduled meetings");
        JobPortalResponse response = new JobPortalResponse();
        response.setData(activeMeeting);
        response.setMessage(messageSource.getMessage("active.interview",null, Locale.getDefault()));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Update scheduled meeting.
     *
     * @param meetingId
     * @param meetingRequest    Scheduled meeting DTO
     * @return scheduled meeting
     */
    @PutMapping("/scheduled-meetings/{meetingId}")
    public ResponseEntity<JobPortalResponse> updateActiveScheduledMeeting(@PathVariable String meetingId,
                                                                          @RequestBody MeetingScheduleRequest meetingRequest) {
        MeetingSchedule schedule = this.meetingService.updateScheduledMeeting(meetingId,meetingRequest);
        log.info("Updated scheduled meeting id {}",meetingId);
        JobPortalResponse response = new JobPortalResponse();
        response.setData(schedule);
        response.setMessage(messageSource.getMessage("meeting.updated",null,Locale.getDefault()));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Delete scheduled meeting.
     *
     * @param meetingId
     * @return deleted scheduled meeting response
     */
    @DeleteMapping("/scheduled-meetings/{meetingId}")
    public ResponseEntity<JobPortalResponse> deleteScheduledMeeting(@PathVariable String meetingId) {
        this.meetingService.deleteScheduledMeeting(meetingId);
        log.info("Scheduled meeting id {} has been deleted",meetingId);
        JobPortalResponse response = new JobPortalResponse();
        response.setMessage(messageSource.getMessage("meeting.deleted",null,Locale.getDefault()));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Get scheduled meeting details.
     *
     * @param meetingId
     * @return meeting schedule.
     */
    @GetMapping("/scheduled-meetings/{meetingId}")
    public ResponseEntity<JobPortalResponse> getScheduledMeeting(@PathVariable String meetingId) {
        ScheduledMeetingResponse meetingSchedule = this.meetingService.getScheduledMeeting(meetingId);
        log.info("Gathered scheduled meeting details for the meeting ID: {}",meetingSchedule.getMeetingSchedule().getMeetingId());
        JobPortalResponse response = new JobPortalResponse();
        response.setData(meetingSchedule);
        response.setMessage(messageSource.getMessage("get.meeting",null,Locale.getDefault()));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Create Zoom meeting.
     *
     * @param request   Meeting schedule DTO
     * @return meeting
     */
    @PostMapping("/scheduled-meetings")
    public ResponseEntity<JobPortalResponse> createMeeting(@RequestBody MeetingScheduleRequest request) {
        Map<String, Object> response = zoomService.createMeeting(request.getMeetingRequest());
        MeetingSchedule schedule = zoomService.assignMeeting(request, response);
        log.info("Meeting has been created :{}",schedule.getMeetingId());
        JobPortalResponse jobPortalResponse = new JobPortalResponse();
        jobPortalResponse.setMessage("New meeting has been created");
        jobPortalResponse.setData(schedule);
        return new ResponseEntity<>(jobPortalResponse, HttpStatus.OK);
    }

    /**
     * To authenticate Zoom client.
     *
     * @return zoom auth response
     */
    @PostMapping("/auth")
    public AuthResponse authClient() {
        return this.zoomService.authenticateZoomClient();
    }

    /**
     *
     * @param jobApplicationId
     * @return meeting
     */
    @GetMapping("/")
    public ResponseEntity<JobPortalResponse> getMeetingByApplication(@RequestParam String jobApplicationId) {
        JobPortalResponse jobPortalResponse = new JobPortalResponse();
        jobPortalResponse.setData(this.zoomService.getMeetingByJobApplication(jobApplicationId));
        return new ResponseEntity<>(jobPortalResponse, HttpStatus.OK);
    }

    /**
     * Get Meeting by job-application id.
     *
     * @param jobApplicationId
     * @return Scheduled meeting details
     */
    @GetMapping("/scheduled-meetings/job-applications/{jobApplicationId}")
    public ResponseEntity<JobPortalResponse> getMeetingByJobApplicationId(@PathVariable String jobApplicationId) {
        ScheduledMeetingResponse meetingSchedule = this.meetingService.getMeetingByJobApplicationId(jobApplicationId);
        JobPortalResponse response = new JobPortalResponse();
        response.setData(meetingSchedule);
        response.setMessage(messageSource.getMessage("get.meeting",null,Locale.getDefault()));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
