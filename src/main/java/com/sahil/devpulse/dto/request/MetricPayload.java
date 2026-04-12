package com.sahil.devpulse.dto.request;

import jakarta.validation.constraints.NotNull;

public record MetricPayload(
        @NotNull(message="CPU usage is required")
        Double cpuUsagePercent,

        @NotNull(message="Memory usage is required")
        Double memoryUsagePercent,

        @NotNull(message="Reponse Time is required")
        Double responseTime,

        Double errorRatePercent,      // optional
        Integer activeConnections
) {
}
