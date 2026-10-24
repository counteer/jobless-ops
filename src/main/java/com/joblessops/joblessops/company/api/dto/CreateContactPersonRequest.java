package com.joblessops.joblessops.company.api.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateContactPersonRequest(
    @NotBlank String name,
    String email,
    String phone,
    String title
) {}