package com.leverx.trugame.web.dto;

import lombok.experimental.SuperBuilder;

@SuperBuilder
public class CustomApiSuccess<T> extends CustomApiResponse {

    public final T data;
}
