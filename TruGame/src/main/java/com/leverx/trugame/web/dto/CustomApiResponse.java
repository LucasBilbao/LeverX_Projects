package com.leverx.trugame.web.dto;

import lombok.experimental.SuperBuilder;

import java.time.Instant;

@SuperBuilder
public abstract class CustomApiResponse {

    public final int status;
    public final Instant timestamp = Instant.now();
}
