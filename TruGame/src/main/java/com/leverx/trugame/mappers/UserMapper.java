package com.leverx.trugame.mappers;

import com.leverx.trugame.entities.UserEntity;
import com.leverx.trugame.requests.users.RegisterUserRequestDto;
import com.leverx.trugame.responses.UserResponseDto;
import com.leverx.trugame.utils.PasswordEncryptor;

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

    public static UserEntity fromRequestToEntity(RegisterUserRequestDto req) {
        return UserEntity.builder()
                .firstName(req.getFirstName())
                .lastName(req.getLastName())
                .email(req.getEmail())
                .password(PasswordEncryptor.hashPassword(req.getPassword()))
                .role(req.getRole())
                .build();
    }
}
