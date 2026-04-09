package com.sahil.devpulse.model;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(name="metric_snapshots")
public class MetricSnapshot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "application_id", nullable = false)
    private Application application;

    private Double cpuUsagePercent;
    private Double memoryUsagePercent;
    private Double responseTimeMs;
    private Double errorRatePercent;
    private Integer activeConnections;

    private LocalDateTime recordedAt = LocalDateTime.now();
}
