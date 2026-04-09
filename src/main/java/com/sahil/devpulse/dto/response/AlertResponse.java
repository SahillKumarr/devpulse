package com.sahil.devpulse.dto.response;

import com.sahil.devpulse.model.enums.AlertSeverity;
import com.sahil.devpulse.model.enums.AlertType;

import java.time.LocalDateTime;

public record AlertResponse(
        Long id,
        Long applicationId,
        String applicationName,
        AlertSeverity severity,
        AlertType type,
        String message,
        boolean resolved,
        LocalDateTime triggeredAt
) {
}
