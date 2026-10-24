package com.joblessops.joblessops.company.api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CompanyExceptionHandler {

    @ExceptionHandler(CompanyNotFoundException.class)
    public ProblemDetail handleCompanyNotFound(CompanyNotFoundException ex) {
        var pd = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        pd.setDetail(ex.getMessage());
        return pd;
    }
}
