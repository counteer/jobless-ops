package com.joblessops.joblessops.jobapplication.api;

import com.joblessops.joblessops.jobapplication.exception.UnknownApplicationStatusException;
import org.springframework.http.HttpStatus;
import com.joblessops.joblessops.jobapplication.exception.JobApplicationNotFoundException;
import com.joblessops.joblessops.jobapplication.exception.JobPostingNotFoundException;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class JobApplicationExceptionHandler {
    @ExceptionHandler(UnknownApplicationStatusException.class)
    public ProblemDetail handleUnknownApplicationStatus(UnknownApplicationStatusException ex) {
        var pd = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        pd.setDetail(ex.getMessage());
        return pd;
    }

    @ExceptionHandler({JobApplicationNotFoundException.class, JobPostingNotFoundException.class})
    public ProblemDetail handleNotFound(RuntimeException ex) {
        var pd = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        pd.setDetail(ex.getMessage());
        return pd;
    }
}
