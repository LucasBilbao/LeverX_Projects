package com.leverx.trugame.web;

import com.leverx.trugame.web.dto.ApiError;
import com.leverx.trugame.web.dto.ApiResponse;
import com.leverx.trugame.web.dto.ApiSuccess;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public final class ResponseFactory {

    public static ResponseEntity<ApiResponse> error(String message, HttpStatus status) {
        return ResponseEntity.status(status).body(
                ApiError.builder()
                        .error(status.getReasonPhrase())
                        .status(status.value())
                        .message(message)
                        .build()
        );
    }

    public static ResponseEntity<ApiResponse> error(String message) {
        return error(message, HttpStatus.NOT_FOUND);
    }

    public static <T> ResponseEntity<ApiResponse> success(T data, HttpStatus status) {
        return ResponseEntity.status(status).body(
                ApiSuccess.builder()
                        .status(status.value())
                        .data(data)
                        .build()
        );
    }

    public static <T> ResponseEntity<ApiResponse> success(T data) {
        return success(data, HttpStatus.OK);
    }
}
