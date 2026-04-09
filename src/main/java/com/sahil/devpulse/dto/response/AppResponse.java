package com.sahil.devpulse.dto.response;

import com.sahil.devpulse.model.enums.AppStatus;

import java.time.LocalDateTime;

public record AppResponse(
        Long id,
        String name,
        String baseUrl,
        String ownerEmail,
        AppStatus status,
        LocalDateTime registeredAt,
        LocalDateTime lastCheckedAt
) {
}
