package com.leverx.trugame.web.dto;

import lombok.experimental.SuperBuilder;

@SuperBuilder
public class ApiSuccess<T> extends ApiResponse {

    public final T data;
}
