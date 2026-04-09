package com.sahil.devpulse.repository;

import com.sahil.devpulse.model.MetricSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.*;

public interface MetricSnapshotRepository extends JpaRepository<MetricSnapshot,Long> {

    // ek app ke saare metrics, latest pehle
    List<MetricSnapshot> findByApplicationIdOrderByRecordedAtDesc(Long applicationId);

    // specific time ke baad ke metrics — "last 1 hour" feature ke liye
    List<MetricSnapshot> findByApplicationIdAndRecordedAtAfter(
            Long applicationId, LocalDateTime after
    );

    // ek app ka sabse latest metric snapshot
    Optional<MetricSnapshot> findTopByApplicationIdOrderByRecordedAtDesc(Long applicationId);
}
