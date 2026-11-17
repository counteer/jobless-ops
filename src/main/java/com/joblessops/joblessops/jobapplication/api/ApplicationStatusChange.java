package com.joblessops.joblessops.jobapplication.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record ApplicationStatusChange(@NotBlank String statusCode, @NotNull LocalDateTime changedAt) {}
