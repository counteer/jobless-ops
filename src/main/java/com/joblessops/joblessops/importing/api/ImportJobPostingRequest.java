package com.joblessops.joblessops.importing.api;

import jakarta.validation.constraints.NotBlank;

public record ImportJobPostingRequest(
        @NotBlank
        String title,
        String url,
        String description
) {}