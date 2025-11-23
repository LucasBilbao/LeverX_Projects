package com.leverx.trugame.responses;

import com.leverx.trugame.entities.Role;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponseDto {

    private Integer id;

    private String firstName;

    private String lastName;

    private String email;

    private Role role;
}
