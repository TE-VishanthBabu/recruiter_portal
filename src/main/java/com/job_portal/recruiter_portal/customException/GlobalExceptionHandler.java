package com.job_portal.recruiter_portal.customException;

import com.job_portal.recruiter_portal.constants.Constant;
import com.job_portal.recruiter_portal.response.ExceptionResponse;
import com.job_portal.recruiter_portal.response.JobPortalResponse;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private final MessageSource messageSource;

    @ExceptionHandler(CommonException.class)
    public ResponseEntity<ExceptionResponse> handleExceptions(CommonException ex) {
        ExceptionResponse response = new ExceptionResponse();
        response.setCode(ex.getStatus().value());
        response.setStatus(ex.getStatus().getReasonPhrase());
        response.setMessage(ex.getMessage());
        response.setDetailedError(ex.getDetailedError());
        return new ResponseEntity<>(response, ex.getStatus());
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ExceptionResponse> handleFileException(MaxUploadSizeExceededException ex) {
        ExceptionResponse response = new ExceptionResponse();
        response.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.setStatus("File upload too big");
        response.setMessage(ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({ ConstraintViolationException.class })
    public ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex, WebRequest request) {
        HashMap<String, Object> response = new HashMap<>();
        List<HashMap<String, Object>> errors = ex.getConstraintViolations()
                .stream()
                .map(x->{
                    HashMap<String, Object> error = new HashMap<>();
                    String field = x.getPropertyPath().toString();
                    String[] list  = field.split("[.]", 0);

                    error.put("field", list[list.length-1]);
                    error.put("errorMessage", x.getMessage());
                    return error;
                }).collect(Collectors.toList());
        response.put("details", errors);
        JobPortalResponse error = new JobPortalResponse(Constant.STATUS_FAILED, HttpStatus.BAD_REQUEST.value(), ex.getMessage(),
                response);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        HashMap<String, Object> response = new HashMap<>();
        List<HashMap<String, Object>> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(x->{
                    HashMap<String, Object> error = new HashMap<>();
                    error.put("field", x.getField());
                    error.put("errorMessage", x.getDefaultMessage());
                    return error;
                }).collect(Collectors.toList());
        response.put("details", errors);
        JobPortalResponse error = new JobPortalResponse(Constant.STATUS_FAILED, HttpStatus.BAD_REQUEST.value(),
                messageSource.getMessage("invalid.arguments",null, Locale.ENGLISH),
                response);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}
