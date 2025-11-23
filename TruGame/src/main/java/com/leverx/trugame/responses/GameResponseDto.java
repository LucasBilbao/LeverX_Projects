package com.leverx.trugame.responses;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class GameResponseDto {

    private Integer id;

    private String title;

    private String text;

    private Integer userId;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
