package com.job_portal.recruiter_portal.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class Settings {
    private Integer approval_type = 2;
    private String auto_recording = "local";
    private boolean email_notification = true;
    private String encryption_type = "enhanced_encryption";
    private Integer jbh_time = 5;
    private boolean join_before_host = true;
    private List<Invitees> meeting_invitees;
    private boolean mute_upon_entry = true;
    private boolean participant_video = false;
    private boolean waiting_room = false;
}