package com.joblessops.joblessops.jobapplication.api.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.UUID;

public record CreateJobApplicationRequest(@NotNull UUID jobPostingId, String notes,
                                          @NotNull LocalDate appliedAt) {}
