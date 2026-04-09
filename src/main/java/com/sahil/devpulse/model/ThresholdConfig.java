package com.sahil.devpulse.model;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name ="threshold_config")
public class ThresholdConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="application_id",unique = true)
    private Application application;

    private Double maxCpuPercent=80.0;
    private Double maxMemoryPercent=85.0;
    private Double maxResponseTimeMs = 2000.0;
    private Double maxErrorRatePercent = 5.0;
}
