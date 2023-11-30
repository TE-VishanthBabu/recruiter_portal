package com.job_portal.recruiter_portal.request;

import jakarta.persistence.ElementCollection;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobApplicationFilterRequest {
    @ElementCollection
    private List<String> appliedDate;
    @ElementCollection
    private List<String> workExp;
}
