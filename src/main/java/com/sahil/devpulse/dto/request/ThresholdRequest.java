package com.sahil.devpulse.dto.request;

public record ThresholdRequest(
        Double maxCpuPercent,
        Double maxMemoryPercent,
        Double maxReponseTime,
        Double maxErrorRatePercent
) {
}
