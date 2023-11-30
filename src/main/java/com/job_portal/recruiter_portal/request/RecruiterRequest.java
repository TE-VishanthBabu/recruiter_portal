package com.job_portal.recruiter_portal.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecruiterRequest {
    private String userName;
    private String email;
    private String password;
    private String confirmPassword;
    private String firstName;
    private String lastName;
}
