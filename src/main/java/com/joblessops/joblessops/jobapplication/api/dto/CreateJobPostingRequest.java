package com.joblessops.joblessops.jobapplication.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateJobPostingRequest(
        @NotNull UUID companyId,
        @NotBlank String title,
        String url,
        String description
) {
}