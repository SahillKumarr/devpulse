package com.sahil.devpulse.controller;


import com.sahil.devpulse.dto.request.RegisterAppRequest;
import com.sahil.devpulse.dto.request.ThresholdRequest;
import com.sahil.devpulse.dto.response.ApiResponse;
import com.sahil.devpulse.dto.response.AppResponse;
import com.sahil.devpulse.dto.response.ThresholdResponse;
import com.sahil.devpulse.service.ApplicationService;
import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/apps")
@RequiredArgsConstructor
@Validated
public class ApplicationController {

    private final ApplicationService applicationService;

    @PostMapping
    public ResponseEntity<ApiResponse<AppResponse>> register(@Valid @RequestBody RegisterAppRequest request){
        AppResponse response= applicationService.registerApplication(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Application Registered Successfully", response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<AppResponse>>> getAll(){
        return ResponseEntity.ok(
                ApiResponse.ok("Applications fetched Successfully", applicationService.getAllApplications())
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AppResponse>> getById(@PathVariable Long id){
        return ResponseEntity.ok(
                ApiResponse.ok("Application fetched Successfully", applicationService.getApplicationById(id))
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<AppResponse>> delete(@PathVariable Long id){
        return ResponseEntity.ok(
                ApiResponse.ok("Application Deleted Successfully",null)
        );
    }

    @PutMapping("/{id}/thresholds")
    public ResponseEntity<ApiResponse<ThresholdResponse>> updateThreshold(@PathVariable Long id, @Valid @RequestBody ThresholdRequest request){
        ThresholdResponse response= applicationService.updateThresholds(id,request);
        return ResponseEntity.ok(
                ApiResponse.ok("Application Threshold updated Successfully", response)
        );
    }
}
