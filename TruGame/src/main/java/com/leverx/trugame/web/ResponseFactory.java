package com.leverx.trugame.web;

import com.leverx.trugame.web.dto.CustomApiError;
import com.leverx.trugame.web.dto.CustomApiResponse;
import com.leverx.trugame.web.dto.CustomApiSuccess;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public final class ResponseFactory {

    public static ResponseEntity<CustomApiResponse> error(String message, HttpStatus status) {
        return ResponseEntity.status(status).body(
                CustomApiError.builder()
                        .error(status.getReasonPhrase())
                        .status(status.value())
                        .message(message)
                        .build()
        );
    }

    public static ResponseEntity<CustomApiResponse> error(String message) {
        return error(message, HttpStatus.NOT_FOUND);
    }

    public static <T> ResponseEntity<CustomApiResponse> success(T data, HttpStatus status) {
        return ResponseEntity.status(status).body(
                CustomApiSuccess.builder()
                        .status(status.value())
                        .data(data)
                        .build()
        );
    }

    public static <T> ResponseEntity<CustomApiResponse> success(T data) {
        return success(data, HttpStatus.OK);
    }
}
