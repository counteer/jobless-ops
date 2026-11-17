package com.joblessops.joblessops.jobapplication.api.dto;

import com.joblessops.joblessops.jobapplication.domain.ApplicationStatus;
import java.util.UUID;

public record ApplicationStatusResponse(UUID id, String code, String displayName, boolean active) {
    public static ApplicationStatusResponse from(ApplicationStatus status) {
        return new ApplicationStatusResponse(status.getId(), status.getCode(), status.getDisplayName(), status.isActive());
    }
}
