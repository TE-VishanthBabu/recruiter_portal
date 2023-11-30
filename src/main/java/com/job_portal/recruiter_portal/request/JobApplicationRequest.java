package com.job_portal.recruiter_portal.request;

import com.job_portal.recruiter_portal.entity.Notes;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobApplicationRequest {
    private List<Notes> notes;
    private String status;
}
