package com.joblessops.joblessops.company.api.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record ContactPersonResponse(
    UUID id,
    @NotBlank String name,
    String email,
    String phone,
    String title,
    UUID companyId
) {}