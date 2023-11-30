package com.job_portal.recruiter_portal.response;

import com.job_portal.recruiter_portal.constants.Constant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobPortalResponse {
    private String status = Constant.STATUS_SUCCESS;
    private Integer code = HttpStatus.OK.value();
    private String message;
    private Object data;
}
