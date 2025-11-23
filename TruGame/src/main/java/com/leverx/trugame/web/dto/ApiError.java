package com.leverx.trugame.web.dto;

import lombok.experimental.SuperBuilder;

@SuperBuilder
public class ApiError extends ApiResponse {

    public final String error;
    public final String message;
}
