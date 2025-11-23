package com.leverx.trugame.requests.users;

import com.leverx.trugame.entities.Role;
import com.leverx.trugame.validators.PasswordMatches;
import lombok.Getter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@PasswordMatches(message = "Password and confirmation must match.")
public class RegisterUserRequestDto extends UserRequestDto {

    @NotNull(message = "Role must not be empty.")
    private final Role role = Role.ANONYMOUS;

    @NotNull(message = "First name must not be null.")
    @NotEmpty(message = "First name must not be empty.")
    @Size(
            min = 1,
            max = 50,
            message = "First name must contain a minimum of 1 and a maximum of 50 characters."
    )
    private String firstName;

    @NotNull(message = "Last name must not be null.")
    @NotEmpty(message = "Last name must not be empty.")
    @Size(
            min = 1,
            max = 50,
            message = "Last name must contain a minimum of 1 and a maximum of 50 characters."
    )
    private String lastName;

    @NotNull(message = "Confirm password must not be null.")
    @NotEmpty(message = "Confirm password must not be empty.")
    @Size(
            min = 5,
            max = 25,
            message = "Confirm password must contain a minimum of 5 and a maximum of 25 characters."
    )
    private String confirmPassword;
}
