package com.joblessops.joblessops.importing.api;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record ImportCompanyRequest(
        @NotBlank
        String name,
        String website,
        @Valid List<ImportJobPostingRequest> jobPostings
) {}