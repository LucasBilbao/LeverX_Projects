package com.leverx.trugame.mappers;

import com.leverx.trugame.entities.UserEntity;
import com.leverx.trugame.responses.UserResponseDto;

public class UserMapper {

    public static UserResponseDto fromEntityToResponse(UserEntity user) {
        return UserResponseDto.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }
}
