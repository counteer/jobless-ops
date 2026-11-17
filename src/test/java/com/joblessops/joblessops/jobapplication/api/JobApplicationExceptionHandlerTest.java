package com.joblessops.joblessops.jobapplication.api;

import com.joblessops.joblessops.jobapplication.exception.UnknownApplicationStatusException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JobApplicationExceptionHandlerTest {
    @Test
    void returnsBadRequestWithErrorDetail() {
        var problem = new JobApplicationExceptionHandler().handleUnknownApplicationStatus(new UnknownApplicationStatusException("Invalid status"));

        assertEquals(400, problem.getStatus());
        assertEquals("Invalid status", problem.getDetail());
    }
}
