package com.sahil.devpulse.dto.response;

public record ThresholdResponse(
        Long id,
        Long applicationId,
        Double maxCpuPercent,
        Double maxMemoryPercent,
        Double maxResponseTimeMs,
        Double maxErrorRatePercent
) {}