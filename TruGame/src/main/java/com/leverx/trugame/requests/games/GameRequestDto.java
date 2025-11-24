package com.leverx.trugame.requests.games;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GameRequestDto {

    @NotBlank(message = "Title must not be blank.")
    @Size(
            min = 3,
            max = 50,
            message = "Title must contain a minimum of 3 and a maximum of 50 characters."
    )
    private String title;

    @NotBlank(message = "Text must not be blank.")
    @Size(
            min = 5,
            max = 255,
            message = "Text must contain a minimum of 5 and a maximum of 255 characters."
    )
    private String text;
}
