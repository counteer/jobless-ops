package com.joblessops.joblessops.importing.api;

import com.joblessops.joblessops.importing.api.exceptions.InvalidImportException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ImportExceptionHandlerTest {
    @Test
    void returnsBadRequestWithErrorDetail() {
        var problem = new ImportExceptionHandler().handleInvalidImport(new InvalidImportException("Invalid status"));

        assertEquals(400, problem.getStatus());
        assertEquals("Invalid status", problem.getDetail());
    }
}
