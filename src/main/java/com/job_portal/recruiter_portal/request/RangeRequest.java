package com.job_portal.recruiter_portal.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RangeRequest {
    private Integer min;
    private Integer max;
}
