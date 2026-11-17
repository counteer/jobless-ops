package com.joblessops.joblessops.jobapplication.api.dto;

import java.time.LocalDate;
import java.util.UUID;

public record JobApplicationResponse(UUID id, UUID jobPostingId, String notes,
                                     LocalDate appliedAt, String statusCode) {}
