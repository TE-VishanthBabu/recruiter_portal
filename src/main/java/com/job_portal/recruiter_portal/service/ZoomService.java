package com.job_portal.recruiter_portal.service;

import com.google.gson.Gson;
import com.job_portal.recruiter_portal.constants.Constant;
import com.job_portal.recruiter_portal.customException.CommonException;
import com.job_portal.recruiter_portal.entity.MeetingSchedule;
import com.job_portal.recruiter_portal.repository.MeetingScheduleRepository;
import com.job_portal.recruiter_portal.request.MeetingRequest;
import com.job_portal.recruiter_portal.request.MeetingScheduleRequest;
import com.job_portal.recruiter_portal.response.AuthResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.tomcat.websocket.Constants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class ZoomService {
    @Value("${zoom.baseUrl}")
    private String baseUrl;

    @Value("${zoom.client.accountId}")
    private String accountId;

    @Value("${zoom.client.clientId}")
    private String clientId;

    @Value("${zoom.client.clientSecret}")
    private String clientSecret;

    @Value("${zoom.client.grantType}")
    private String grantType;

    @Value("${zoom.apiUrl}")
    private String apiUrl;

    @Value("${zoom.client.userId}")
    private String userId;

    private final RestTemplate restTemplate;
    private final MeetingScheduleRepository scheduleRepository;


    public AuthResponse authenticateZoomClient(){
        HttpHeaders headers = new HttpHeaders();
        String auth = clientId + ":" + clientSecret;
        String base64Creds = Base64.getEncoder().encodeToString(auth.getBytes());
        headers.add(Constants.AUTHORIZATION_HEADER_NAME, "Basic " + base64Creds);
        String url = baseUrl + "/oauth/token";
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url).queryParam(Constant.GRANT_TYPE, grantType)
                .queryParam(Constant.ACCOUNT_ID, accountId);
        url = builder.toUriString();
        HttpEntity< String > request = new HttpEntity(headers);
        return this.restTemplate.postForEntity(url, request, AuthResponse.class).getBody();
    }


    /**
     * Zoom meeting api call.
     *
     * @param meetingRequest
     * @return Zoom meeting response
     */
    public Map<String, Object> createMeeting(MeetingRequest meetingRequest){
        HttpHeaders headers = new HttpHeaders();
        AuthResponse authResponse = this.authenticateZoomClient();
        headers.add(Constants.AUTHORIZATION_HEADER_NAME, authResponse.getTokenType() + " " + authResponse.getAccessToken());
        String url = apiUrl + "/users/{userId}/meetings";
        Map<String, String> params = new HashMap<>();
        params.put("userId", userId);
        UriComponents builder = UriComponentsBuilder.fromHttpUrl(url).buildAndExpand(params);
        url = builder.toUriString();
        HttpEntity< String > request = new HttpEntity(meetingRequest, headers);
        return this.restTemplate.postForEntity(url, request, Map.class).getBody();
    }

    /**
     * Storing scheduled meeting details to db.
     *
     * @param request
     * @param meetingResponse
     * @return Scheduled meeting
     */
    public MeetingSchedule assignMeeting(MeetingScheduleRequest request, Map<String, Object> meetingResponse){
        MeetingSchedule schedule = new MeetingSchedule();
        try {
            schedule.setRecruiterId(request.getRecruiterId());
            schedule.setJobApplicationId(request.getJobApplicationId());
            schedule.setJobSeekerId(request.getJobSeekerId());
            schedule.setMeetingId(meetingResponse.get("id").toString());
            schedule.setPassword(meetingResponse.get("password").toString());
            schedule.setDuration(meetingResponse.get("duration").toString());

            LocalDateTime startTime = LocalDateTime.parse(meetingResponse.get("start_time").toString(), DateTimeFormatter.ISO_DATE_TIME);
            Duration timeDifference = Duration.ofHours(-5).plusMinutes(-30); // -5:30

            LocalDateTime newTime = startTime.minus(timeDifference);

            String dateTime = newTime.format(DateTimeFormatter.ISO_DATE_TIME);

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date startMeetingTime = dateFormat.parse(dateTime);
            String formattedDate = formatter.format(startMeetingTime);
            startMeetingTime = formatter.parse(formattedDate);
            schedule.setStartTime(startMeetingTime);
            schedule.setTopic(meetingResponse.get("topic").toString());
            schedule.setAgenda(meetingResponse.get("agenda").toString());
            schedule.setMeetingResponse(meetingResponse.toString());
            schedule.setLocation(request.getLocation());
            schedule.setInterviewStatus("In-Progress");
        }catch (Exception e){
            e.printStackTrace();
        }
        return this.scheduleRepository.save(schedule);
    }

    public Map<String, Object> getMeetingByJobApplication(String jobApplicationId){
        MeetingSchedule schedule = this.scheduleRepository.findByJobApplicationId(jobApplicationId).orElseThrow(()->{
            throw new CommonException("Schedule not found for job application", HttpStatus.NOT_FOUND);
        });
        Gson gson = new Gson();
        return gson.fromJson(gson.toJson(schedule.getMeetingResponse()),Map.class);
    }

    /**
     * Get Zoom meeting by meeting id.
     *
     * @param meetingId
     * @return get meeting
     */
    public Map<String, Object> getMeeting(String meetingId) {
        HttpHeaders headers = new HttpHeaders();
        AuthResponse authResponse = this.authenticateZoomClient();
        headers.add(Constants.AUTHORIZATION_HEADER_NAME, authResponse.getTokenType() + " " + authResponse.getAccessToken());
        String url = apiUrl + "/meetings/{meetingId}";
        Map<String, String> params = new HashMap<>();
        params.put("meetingId", meetingId);
        UriComponents builder = UriComponentsBuilder.fromHttpUrl(url).buildAndExpand(params);
        url = builder.toUriString();
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity< String > request = new HttpEntity(headers);
        Map<String,Object> response = restTemplate.exchange(url, HttpMethod.GET, request, Map.class).getBody();
        return response;
    }

    /**
     * Update zoom meeting.
     * @param meetingId
     * @param meetingRequest
     */
    public void updateMeeting(String meetingId, MeetingRequest meetingRequest) {
        HttpHeaders headers = new HttpHeaders();
        AuthResponse authResponse = this.authenticateZoomClient();
        headers.add(Constants.AUTHORIZATION_HEADER_NAME, authResponse.getTokenType() + " " + authResponse.getAccessToken());
        String url = apiUrl + "/meetings/{meetingId}";
        Map<String, String> params = new HashMap<>();
        params.put("meetingId", meetingId);
        UriComponents builder = UriComponentsBuilder.fromHttpUrl(url).buildAndExpand(params);
        url = builder.toUriString();
        RestTemplate restTemplate = new RestTemplate();
        HttpClient httpClient = HttpClientBuilder.create().build();
        restTemplate.setRequestFactory(new
                HttpComponentsClientHttpRequestFactory(httpClient));
        HttpEntity< String > request = new HttpEntity(meetingRequest,headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PATCH, request, String.class);
        log.info("Updated meeting status code :{}",response.getStatusCode());
    }

    /**
     * Storing updated scheduled meeting.
     *
     * @param request   Meeting schedule DTO
     * @param map
     * @return meeting
     */
    public MeetingSchedule saveMeeting(MeetingScheduleRequest request, Map<String, Object> map){
        MeetingSchedule schedule = null;
        try {
        schedule = scheduleRepository.findByMeetingId(map.get("id").toString());
            LocalDateTime startTime = LocalDateTime.parse(map.get("start_time").toString(), DateTimeFormatter.ISO_DATE_TIME);
            Duration timeDifference = Duration.ofHours(-5).plusMinutes(-30); // -5:30

            LocalDateTime newTime = startTime.minus(timeDifference);

            String dateTime = newTime.format(DateTimeFormatter.ISO_DATE_TIME);

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date startMeetingTime = dateFormat.parse(dateTime);
            startMeetingTime = formatter.parse(formatter.format(startMeetingTime));
            schedule.setStartTime(startMeetingTime);
        schedule.setLocation(request.getLocation());
        schedule.setDuration(map.get("duration").toString());
        }catch (Exception e) {
            e.printStackTrace();
        }
        return scheduleRepository.save(schedule);
    }

    /**
     * Delete zoom meeting.
     *
     * @param meetingId
     */
    public void deleteMeeting(String meetingId) {
        HttpHeaders headers = new HttpHeaders();
        AuthResponse authResponse = this.authenticateZoomClient();
        headers.add(Constants.AUTHORIZATION_HEADER_NAME, authResponse.getTokenType() + " " + authResponse.getAccessToken());
        String url = apiUrl + "/meetings/{meetingId}";
        Map<String, String> params = new HashMap<>();
        params.put("meetingId", meetingId);
        UriComponents builder = UriComponentsBuilder.fromHttpUrl(url).buildAndExpand(params);
        url = builder.toUriString();
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity< String > request = new HttpEntity(headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.DELETE, request, String.class);
        log.info("Zoom meeting Id deleted status :{}",response.getStatusCode());
    }
}
