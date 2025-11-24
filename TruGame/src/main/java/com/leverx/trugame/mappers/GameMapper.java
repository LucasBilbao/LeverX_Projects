package com.leverx.trugame.mappers;

import com.leverx.trugame.entities.GameEntity;
import com.leverx.trugame.entities.UserEntity;
import com.leverx.trugame.requests.games.CreateGameRequestDto;
import com.leverx.trugame.responses.GameResponseDto;

public class GameMapper {

    public static GameResponseDto fromEntityToResponse(GameEntity game) {
        return GameResponseDto.builder()
                .id(game.getId())
                .title(game.getTitle())
                .text(game.getText())
                .userId(game.getUser().getId())
                .createdAt(game.getCreatedAt())
                .updatedAt(game.getUpdatedAt())
                .build();
    }

    public static GameEntity fromRequestToEntity(CreateGameRequestDto req, UserEntity user) {
        return GameEntity.builder()
                .title(req.getTitle())
                .text(req.getText())
                .user(user)
                .build();
    }
}
