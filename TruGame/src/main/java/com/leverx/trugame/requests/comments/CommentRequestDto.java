package com.leverx.trugame.requests.comments;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommentRequestDto {

    @NotBlank(message = "Message must not be blank.")
    @Size(
            min = 15,
            max = 255,
            message = "Message must contain a minimum of 15 and a maximum of 255 characters."
    )
    private String message;

    @Min(value = 1, message = "Rating must be one of the following: 1, 2, 3, 4, 5")
    @Max(value = 5, message = "Rating must be one of the following: 1, 2, 3, 4, 5")
    private Short rating;
}
