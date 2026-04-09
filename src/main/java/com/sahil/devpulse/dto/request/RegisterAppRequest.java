package com.sahil.devpulse.dto.request;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RegisterAppRequest (
    @NotBlank(message="App Name is Required")
    String name,

    @NotBlank(message = "")
    String baseUrl,

    @NotBlank(message = "")
    @Email(message = "Invalid email format")
    String ownerEmail
)
{}
