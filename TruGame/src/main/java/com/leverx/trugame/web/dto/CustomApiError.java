package com.leverx.trugame.web.dto;

import lombok.experimental.SuperBuilder;

@SuperBuilder
public class CustomApiError extends CustomApiResponse {

    public final String error;
    public final String message;
}
