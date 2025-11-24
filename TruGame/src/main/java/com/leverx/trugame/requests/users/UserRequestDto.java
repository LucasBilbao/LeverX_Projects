package com.leverx.trugame.requests.users;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDto {

    @Email(message = "Value must be an email.")
    @NotNull(message = "Email must not be null.")
    @NotEmpty(message = "Email must not be empty.")
    @Size(
            min = 3,
            max = 50,
            message = "Email must contain a minimum of 3 and a maximum of 50 characters."
    )
    private String email;

    @NotNull(message = "Password must not be null.")
    @NotEmpty(message = "Password must not be empty.")
    @Size(
            min = 5,
            max = 25,
            message = "Password must contain a minimum of 5 and a maximum of 25 characters."
    )
    private String password;
}
