package com.job_portal.recruiter_portal.customException;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
public class CommonException extends RuntimeException{
    private final HttpStatus status;
    private final String detailedError;

    public CommonException(String message, HttpStatus status, String detailedMessage) {
        super(message);
        this.status = status;
        this.detailedError = detailedMessage;
    }

    public CommonException(String message) {
        super(message);
        this.status = HttpStatus.BAD_REQUEST;
        this.detailedError = null;
    }

    public CommonException(String message, HttpStatus status) {
        super(message);
        this.status = status;
        this.detailedError = null;
    }

    public CommonException(String message, HttpStatus status, Throwable e) {
        super(message);
        this.status = status;
        this.detailedError = e.getMessage();
    }
}
