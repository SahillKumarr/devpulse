package com.sahil.devpulse.service;

import com.sahil.devpulse.dto.request.MetricPayload;
import com.sahil.devpulse.dto.response.MetricResponse;
import com.sahil.devpulse.exception.ResourceNotFoundException;
import com.sahil.devpulse.model.Application;
import com.sahil.devpulse.model.MetricSnapshot;
import com.sahil.devpulse.model.ThresholdConfig;
import com.sahil.devpulse.model.enums.AlertSeverity;
import com.sahil.devpulse.model.enums.AlertType;
import com.sahil.devpulse.model.enums.AppStatus;
import com.sahil.devpulse.repository.ApplicationRepository;
import com.sahil.devpulse.repository.MetricSnapshotRepository;
import com.sahil.devpulse.repository.ThresholdConfigRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MetricService {

    private final MetricSnapshotRepository metricSnapshotRepository;
    private final ApplicationRepository applicationRepository;
    private final ThresholdConfigRepository thresholdConfigRepository;
    private final AlertService alertService;

    // Metrics receive karo + threshold check karo
    @Transactional
    public MetricResponse ingestMetric(Long appId, MetricPayload payload) {

        // App exist karti hai?
        Application app = applicationRepository.findById(appId)
                .orElseThrow(() -> new ResourceNotFoundException("Application", appId));

        // Snapshot save karo
        MetricSnapshot snapshot = new MetricSnapshot();
        snapshot.setApplication(app);
        snapshot.setCpuUsagePercent(payload.cpuUsagePercent());
        snapshot.setMemoryUsagePercent(payload.memoryUsagePercent());
        snapshot.setResponseTimeMs(payload.responseTime());
        snapshot.setErrorRatePercent(payload.errorRatePercent());
        snapshot.setActiveConnections(payload.activeConnections());

        MetricSnapshot saved = metricSnapshotRepository.save(snapshot);

        // App status update karo — UP hai abhi
        AppStatus newStatus;
        if (payload.cpuUsagePercent() > 95 || payload.memoryUsagePercent() > 95) {
            newStatus = AppStatus.DOWN;
        } else if (payload.cpuUsagePercent() > 80 || payload.memoryUsagePercent() > 85
                || payload.responseTime() > 2000) {
            newStatus = AppStatus.DEGRADED;
        } else {
            newStatus = AppStatus.HEALTHY;
        }
        app.setStatus(newStatus);
        app.setLastCheckedAt(LocalDateTime.now());
        applicationRepository.save(app);

        thresholdConfigRepository.findByApplicationId(appId)
                .ifPresent(config -> checkThresholds(app, payload, config));

        log.info("Metrics ingested for app: {} | CPU: {}% | Memory: {}%",
                app.getName(), payload.cpuUsagePercent(), payload.memoryUsagePercent());

        return toResponse(saved);
    }

    // Threshold checking logic
    private void checkThresholds(Application app, MetricPayload payload,
                                 ThresholdConfig config) {

        // CPU check
        if (payload.cpuUsagePercent() != null
                && config.getMaxCpuPercent() != null
                && payload.cpuUsagePercent() > config.getMaxCpuPercent()) {

            AlertSeverity severity = payload.cpuUsagePercent() > 95
                    ? AlertSeverity.CRITICAL : AlertSeverity.WARNING;

            alertService.createAlert(app, AlertType.CPU_HIGH, severity,
                    String.format("CPU usage %.1f%% exceeded threshold %.1f%%",
                            payload.cpuUsagePercent(), config.getMaxCpuPercent()));
        }

        // Memory check
        if (payload.memoryUsagePercent() != null
                && config.getMaxMemoryPercent() != null
                && payload.memoryUsagePercent() > config.getMaxMemoryPercent()) {

            AlertSeverity severity = payload.memoryUsagePercent() > 95
                    ? AlertSeverity.CRITICAL : AlertSeverity.WARNING;

            alertService.createAlert(app, AlertType.MEMORY_HIGH, severity,
                    String.format("Memory usage %.1f%% exceeded threshold %.1f%%",
                            payload.memoryUsagePercent(), config.getMaxMemoryPercent()));
        }

        // Response time check
        if (payload.responseTime() != null
                && config.getMaxResponseTimeMs() != null
                && payload.responseTime() > config.getMaxResponseTimeMs()) {

            alertService.createAlert(app, AlertType.RESPONSE_TIME_HIGH,
                    AlertSeverity.WARNING,
                    String.format("Response time %.0fms exceeded threshold %.0fms",
                            payload.responseTime(), config.getMaxResponseTimeMs()));
        }

        // Error rate check
        if (payload.errorRatePercent() != null
                && config.getMaxErrorRatePercent() != null
                && payload.errorRatePercent() > config.getMaxErrorRatePercent()) {

            AlertSeverity severity = payload.errorRatePercent() > 20
                    ? AlertSeverity.CRITICAL : AlertSeverity.WARNING;

            alertService.createAlert(app, AlertType.ERROR_RATE_HIGH, severity,
                    String.format("Error rate %.1f%% exceeded threshold %.1f%%",
                            payload.errorRatePercent(), config.getMaxErrorRatePercent()));
        }
    }

    // Ek app ke saare metrics
    public List<MetricResponse> getMetricsByApp(Long appId) {
        if (!applicationRepository.existsById(appId)) {
            throw new ResourceNotFoundException("Application", appId);
        }
        return metricSnapshotRepository
                .findByApplicationIdOrderByRecordedAtDesc(appId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // Latest snapshot
    public MetricResponse getLatestMetric(Long appId) {
        return metricSnapshotRepository
                .findTopByApplicationIdOrderByRecordedAtDesc(appId)
                .map(this::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No metrics found for application id: " + appId));
    }

    // Entity → DTO
    private MetricResponse toResponse(MetricSnapshot s) {
        return new MetricResponse(
                s.getId(),
                s.getApplication().getId(),
                s.getApplication().getName(),
                s.getCpuUsagePercent(),
                s.getMemoryUsagePercent(),
                s.getResponseTimeMs(),
                s.getErrorRatePercent(),
                s.getActiveConnections(),
                s.getRecordedAt()
        );
    }
}