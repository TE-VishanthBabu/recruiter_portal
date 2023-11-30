package com.job_portal.recruiter_portal.customException;

public class InvalidFileFormat extends RuntimeException{
    public InvalidFileFormat(String message){
        super(message);
    }
}
