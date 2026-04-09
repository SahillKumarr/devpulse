package com.sahil.devpulse.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public record ErrorResponse(
    boolean success,
    int status,
    String message,
    List<String> errors,
    LocalDateTime timestamp){

    public static ErrorResponse of(int status, String message){
        return new ErrorResponse(false, status,message,null,LocalDateTime.now());
    }

    public static ErrorResponse of(int status, String message,List<String> errors){
        return new ErrorResponse(false,status,message,errors,LocalDateTime.now());
    }

}
