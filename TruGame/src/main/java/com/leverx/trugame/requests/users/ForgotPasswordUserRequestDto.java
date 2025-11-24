package com.leverx.trugame.requests.users;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ForgotPasswordUserRequestDto {

    @NotNull(message = "Email must not be null.")
    @NotBlank(message = "Email must not be blank.")
    @Email(message = "Value passed must be an email")
    private String email;
}
