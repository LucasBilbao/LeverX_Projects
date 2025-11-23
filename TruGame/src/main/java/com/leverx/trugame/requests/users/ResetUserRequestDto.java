package com.leverx.trugame.requests.users;

import com.leverx.trugame.validators.PasswordMatches;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@PasswordMatches(
        passwordField = "newPassword",
        confirmPasswordField = "confirmNewPassword",
        message = "New password and confirmation must match."
)
public class ResetUserRequestDto {

    @NotNull(message = "New password must not be null.")
    @NotBlank(message = "New password must not be blank.")
    @Size(
            min = 5,
            max = 25,
            message = "New password must contain a minimum of 5 and a maximum of 25 characters."
    )
    private String newPassword;

    @NotNull(message = "Confirm new password must not be null.")
    @NotBlank(message = "Confirm new password must not be blank.")
    @Size(
            min = 5,
            max = 25,
            message = "Confirm new password must contain a minimum of 5 and a maximum of 25 characters."
    )
    private String confirmNewPassword;
}
