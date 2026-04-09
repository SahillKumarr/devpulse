package com.sahil.devpulse.dto.response;

public record ApiResponse<T>(
        boolean success,
        String message,
        T data
) {
    // Success response banana easy karne ke liye
    public static <T> ApiResponse<T> ok(String message, T data) {
        return new ApiResponse<>(true, message, data);
    }

    // Error response
    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(false, message, null);
    }
}
