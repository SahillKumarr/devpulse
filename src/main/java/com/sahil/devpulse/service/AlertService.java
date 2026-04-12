package com.sahil.devpulse.service;

import com.sahil.devpulse.dto.response.AlertResponse;
import com.sahil.devpulse.exception.ResourceNotFoundException;
import com.sahil.devpulse.model.Alert;
import com.sahil.devpulse.model.Application;
import com.sahil.devpulse.model.enums.AlertSeverity;
import com.sahil.devpulse.model.enums.AlertType;
import com.sahil.devpulse.repository.AlertRepository;
import com.sahil.devpulse.repository.ApplicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AlertService {

    private final AlertRepository alertRepository;
    private final ApplicationRepository applicationRepository;

    // Ek app ke saare alerts
    public List<AlertResponse> getAlertsByApp(Long appId) {
        if (!applicationRepository.existsById(appId)) {
            throw new ResourceNotFoundException("Application", appId);
        }
        return alertRepository.findByApplicationIdOrderByTriggeredAtDesc(appId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // Sirf unresolved alerts
    public List<AlertResponse> getActiveAlerts(Long appId) {
        return alertRepository.findByApplicationIdAndResolvedFalse(appId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // Alert resolve karo
    @Transactional
    public AlertResponse resolveAlert(Long alertId) {
        Alert alert = alertRepository.findById(alertId)
                .orElseThrow(() -> new ResourceNotFoundException("Alert", alertId));
        alert.setResolved(true);
        return toResponse(alertRepository.save(alert));
    }

    // Alert internally create karne ke liye — MetricService call karega
    @Transactional
    public Alert createAlert(Application app, AlertType type,
                             AlertSeverity severity, String message) {
        Alert alert = new Alert();
        alert.setApplication(app);
        alert.setType(type);
        alert.setSeverity(severity);
        alert.setMessage(message);
        alert.setResolved(false);
        return alertRepository.save(alert);
    }

    // Entity → DTO
    private AlertResponse toResponse(Alert alert) {
        return new AlertResponse(
                alert.getId(),
                alert.getApplication().getId(),
                alert.getApplication().getName(),
                alert.getSeverity(),
                alert.getType(),
                alert.getMessage(),
                alert.isResolved(),
                alert.getTriggeredAt()
        );
    }
}