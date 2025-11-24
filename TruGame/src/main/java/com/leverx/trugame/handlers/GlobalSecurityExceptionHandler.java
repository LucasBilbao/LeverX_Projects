package com.leverx.trugame.handlers;

import com.leverx.trugame.web.ResponseFactory;
import com.leverx.trugame.web.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalSecurityExceptionHandler {

    @ExceptionHandler(AuthenticationCredentialsNotFoundException.class)
    public ResponseEntity<ApiResponse> handleAuthenticationCredentialsNotFound() {
        return ResponseFactory.error("Authentication required", HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse> handleAccessDenied() {
        return ResponseFactory.error("Access Denied", HttpStatus.FORBIDDEN);
    }
}
