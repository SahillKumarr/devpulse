package com.sahil.devpulse.controller;


import com.sahil.devpulse.dto.request.MetricPayload;
import com.sahil.devpulse.dto.response.ApiResponse;
import com.sahil.devpulse.dto.response.MetricResponse;
import com.sahil.devpulse.service.MetricService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/apps/{appId}/metrics")
@RequiredArgsConstructor
public class MetricController {

    private MetricService metricService;

    @PostMapping
    public ResponseEntity<ApiResponse<MetricResponse>> ingest(@PathVariable Long appId, @Valid @RequestBody MetricPayload payload){
        MetricResponse response = metricService.ingestMetric(appId,payload);
        return ResponseEntity.ok(
                ApiResponse.ok("Metric fetched Successfully", response)
        );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<MetricResponse>>> getAll(@PathVariable Long appId){
        return ResponseEntity.ok(
                ApiResponse.ok("Metrics fetched Successfully", metricService.getMetricsByApp(appId))
        );
    }

    @GetMapping("/latest")
    public ResponseEntity<ApiResponse<MetricResponse>> getLatest(
            @PathVariable Long appId){
        return ResponseEntity.ok(
                ApiResponse.ok("Latest Metrics Fetched successfully",metricService.getLatestMetric(appId)));
    }
}
