package com.sahil.devpulse.repository;

import com.sahil.devpulse.model.Alert;
import com.sahil.devpulse.model.enums.AlertSeverity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;


public interface AlertRepository extends JpaRepository<Alert,Long> {
    List<Alert> findByApplicationIdOrderByTriggeredAtDesc(Long applicationId);

    List<Alert> findByApplicationIdAndResolvedFalse(Long applicationId);

    List<Alert> findBySeverityAndResolvedFalse(AlertSeverity severity);
}
