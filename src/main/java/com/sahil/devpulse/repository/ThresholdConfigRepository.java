package com.sahil.devpulse.repository;

import com.sahil.devpulse.model.ThresholdConfig;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ThresholdConfigRepository extends JpaRepository<ThresholdConfig,Long> {

    Optional<ThresholdConfig> findByApplicationId(Long applicationId);
}
