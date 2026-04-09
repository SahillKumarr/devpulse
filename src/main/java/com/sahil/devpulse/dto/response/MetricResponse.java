package com.sahil.devpulse.dto.response;

import java.time.LocalDateTime;

public record MetricResponse(
        Long id,
        Long applicationId,
        String applicationName,
        Double cpuUsagePercent,
        Double memoryUsagePercent,
        Double responseTimeMs,
        Double errorRatePercent,
        Integer activeConnections,
        LocalDateTime recordedAt
) {
}
