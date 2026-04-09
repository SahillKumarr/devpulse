package com.sahil.devpulse.service;


import com.sahil.devpulse.dto.response.AlertResponse;
import com.sahil.devpulse.repository.ApplicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AlertService {

    private final AlertResponse alertResponse;
    private final ApplicationRepository applicationRepository;

}
