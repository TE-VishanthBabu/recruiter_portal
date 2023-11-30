package com.job_portal.recruiter_portal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableAsync
@EnableScheduling
public class RecruiterPortalApplication {

    public static void main(String[] args) {
        SpringApplication.run(RecruiterPortalApplication.class, args);
    }

}
