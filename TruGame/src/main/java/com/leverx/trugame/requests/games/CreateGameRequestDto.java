package com.leverx.trugame.requests.games;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateGameRequestDto extends GameRequestDto {

    @NotNull(message = "User id id must not be null.")
    @Positive(message = "User id must be a positive number.")
    private Integer userId;
}
