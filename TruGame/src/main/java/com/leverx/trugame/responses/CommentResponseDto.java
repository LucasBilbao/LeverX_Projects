package com.leverx.trugame.responses;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class CommentResponseDto {

    private int id;

    private String message;

    private short rating;

    private boolean isApproved;

    private int authorId;

    private int gameId;

    private LocalDateTime createdAt;
}
