package com.sahil.devpulse.controller;


import com.sahil.devpulse.dto.response.AlertResponse;
import com.sahil.devpulse.dto.response.ApiResponse;
import com.sahil.devpulse.dto.response.AppResponse;
import com.sahil.devpulse.service.AlertService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/apps/{appId}/alerts")
@RequiredArgsConstructor
public class AlertController {

    private final AlertService alertService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<AlertResponse>>> getAll(@PathVariable Long appId){
        return ResponseEntity.ok(
                ApiResponse.ok("Alerts Fetched Successfully", alertService.getAlertsByApp(appId))
        );
    }

    @GetMapping("/active")
    public ResponseEntity<ApiResponse<List<AlertResponse>>> getActive(@PathVariable Long appId){
        return ResponseEntity.ok(
                ApiResponse.ok("Active Alerts fetched successfully",alertService.getActiveAlerts(appId))
        );
    }

    @GetMapping("/{alertId}/resolve")
    public ResponseEntity<ApiResponse<AlertResponse>> resolve(@PathVariable Long appId,@PathVariable Long alertId){
        return ResponseEntity.ok(
                ApiResponse.ok("Alert Resolved Successfully",alertService.resolveAlert(alertId))
        );
    }
}
