package com.joblessops.joblessops.importing.api;

import com.joblessops.joblessops.jobapplication.api.ApplicationStatusChange;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

public record ImportJobApplicationRequest(String notes, @NotNull LocalDate appliedAt,
                                          String statusCode,
                                          @Valid List<ApplicationStatusChange> statusHistory) {
    public ImportJobApplicationRequest {
        statusHistory = statusHistory == null ? List.of() : List.copyOf(statusHistory);
    }

    public ImportJobApplicationRequest(String notes, LocalDate appliedAt) {
        this(notes, appliedAt, null, List.of());
    }
}
