package com.joblessops.joblessops.company.api.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateCompanyRequest(
    @NotBlank String name,
    String website
) {}