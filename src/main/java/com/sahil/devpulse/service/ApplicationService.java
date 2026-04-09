package com.sahil.devpulse.service;


import com.sahil.devpulse.dto.request.RegisterAppRequest;
import com.sahil.devpulse.dto.request.ThresholdRequest;
import com.sahil.devpulse.dto.response.AppResponse;
import com.sahil.devpulse.exception.DuplicateResourceException;
import com.sahil.devpulse.exception.ResourceNotFoundException;
import com.sahil.devpulse.model.Application;
import com.sahil.devpulse.model.ThresholdConfig;
import com.sahil.devpulse.model.enums.AppStatus;
import com.sahil.devpulse.repository.ApplicationRepository;
import com.sahil.devpulse.repository.ThresholdConfigRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final ThresholdConfigRepository thresholdConfigRepository;

    @Transactional
    public AppResponse registerApplication(RegisterAppRequest request){

        if(applicationRepository.existsByName(request.name())){
            throw new DuplicateResourceException(
                    "Application already registered with name: "+ request.name()
            );
        }
        Application app= new Application();
        app.setName(request.name());
        app.setBaseUrl(request.baseUrl());
        app.setOwnerEmail(request.ownerEmail());
        app.setStatus(AppStatus.UNKNOWN);

        Application saved= applicationRepository.save(app);

        ThresholdConfig defaultConfig = new ThresholdConfig();
        defaultConfig.setApplication(saved);
        defaultConfig.setMaxCpuPercent(80.0);       // 80% CPU default
        defaultConfig.setMaxMemoryPercent(85.0);     // 85% memory default
        defaultConfig.setMaxResponseTimeMs(2000.0);  // 2 seconds default
        defaultConfig.setMaxErrorRatePercent(5.0);   // 5% error rate default
        thresholdConfigRepository.save(defaultConfig);

        return toResponse(saved);
    }

    // Sab apps lao
    public List<AppResponse> getAllApplications() {
        return applicationRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // ID se ek app lao
    public AppResponse getApplicationById(Long id) {
        Application app = applicationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Application", id));
        return toResponse(app);
    }

    // App delete karo
    @Transactional
    public void deleteApplication(Long id) {
        if (!applicationRepository.existsById(id)) {
            throw new ResourceNotFoundException("Application", id);
        }
        applicationRepository.deleteById(id);
    }

    // Status update karo — internally use hoga scheduler mein
    @Transactional
    public void updateStatus(Long id, AppStatus status) {
        Application app = applicationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Application", id));
        app.setStatus(status);
        applicationRepository.save(app);
    }

    // Entity → DTO conversion
    private AppResponse toResponse(Application app) {
        return new AppResponse(
                app.getId(),
                app.getName(),
                app.getBaseUrl(),
                app.getOwnerEmail(),
                app.getStatus(),
                app.getRegisteredAt(),
                app.getLastCheckedAt()
        );
    }


}
