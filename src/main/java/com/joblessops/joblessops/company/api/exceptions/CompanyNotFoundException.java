package com.joblessops.joblessops.company.api.exceptions;

public class CompanyNotFoundException extends RuntimeException {

    public CompanyNotFoundException(String message) {
        super(message);
    }
}
