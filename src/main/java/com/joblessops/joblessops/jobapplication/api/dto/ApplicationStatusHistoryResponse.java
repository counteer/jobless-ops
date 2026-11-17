package com.joblessops.joblessops.jobapplication.api.dto;

import com.joblessops.joblessops.jobapplication.domain.ApplicationStatusHistory;
import java.time.LocalDateTime;

public record ApplicationStatusHistoryResponse(String statusCode, LocalDateTime changedAt) {
    public static ApplicationStatusHistoryResponse from(ApplicationStatusHistory history) {
        return new ApplicationStatusHistoryResponse(history.getStatus().getCode(), history.getChangedAt());
    }
}
