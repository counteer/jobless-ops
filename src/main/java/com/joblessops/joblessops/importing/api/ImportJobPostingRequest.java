package com.joblessops.joblessops.importing.api;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import java.util.List;

public record ImportJobPostingRequest(
        @NotBlank
        String title,
        String url,
        String description,
        @Valid List<ImportJobApplicationRequest> jobApplications
) {
    public ImportJobPostingRequest {
        jobApplications = jobApplications == null ? List.of() : List.copyOf(jobApplications);
    }

    public ImportJobPostingRequest(String title, String url, String description) {
        this(title, url, description, List.of());
    }
}
