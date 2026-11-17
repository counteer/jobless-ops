package com.joblessops.joblessops.importing.api;

import com.joblessops.joblessops.importing.api.exceptions.InvalidImportException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ImportExceptionHandler {
    @ExceptionHandler(InvalidImportException.class)
    public ProblemDetail handleInvalidImport(InvalidImportException ex) {
        var pd = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        pd.setDetail(ex.getMessage());
        return pd;
    }
}
