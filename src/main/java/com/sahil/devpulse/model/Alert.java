package com.sahil.devpulse.model;


import com.sahil.devpulse.model.enums.AlertSeverity;
import com.sahil.devpulse.model.enums.AlertType;
import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name="alerts")
@Data
@NoArgsConstructor
public class Alert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "application_id", nullable = false)
    private Application application;

    @Enumerated(EnumType.STRING)
    private AlertSeverity severity;

    @Enumerated(EnumType.STRING)
    private AlertType type;

    private String message;

    private boolean resolved = false;
    private LocalDateTime triggeredAt = LocalDateTime.now();
    private LocalDateTime resolvedAt;
}
