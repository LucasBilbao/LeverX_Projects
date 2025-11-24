package com.leverx.trugame.controllers;

import com.leverx.trugame.mappers.UserMapper;
import com.leverx.trugame.requests.users.AuthenticateUserRequestDto;
import com.leverx.trugame.requests.users.ForgotPasswordUserRequestDto;
import com.leverx.trugame.requests.users.RegisterUserRequestDto;
import com.leverx.trugame.requests.users.ResetUserRequestDto;
import com.leverx.trugame.services.UserService;
import com.leverx.trugame.web.ResponseFactory;
import com.leverx.trugame.web.dto.CustomApiResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.PermitAll;
import javax.validation.Valid;
import java.util.Map;

@RequiredArgsConstructor

@RestController
@RequestMapping("/auth")
@Api(value = "User Management", tags = {"User Controller"})
public class UserController extends BaseController {

    private final UserService userService;

    @Operation(summary = "Register user", description = "Register new user")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User was registered successfully"),
            @ApiResponse(code = 404, message = "User could not be registered"),
    })
    @PermitAll
    @PostMapping("/register")
    public ResponseEntity<CustomApiResponse> registerUser(
            @Valid
            @RequestBody
            RegisterUserRequestDto req
    ) {
        return this.run(() ->
                ResponseFactory.success(
                        UserMapper.fromEntityToResponse(
                                this.userService.saveNewUser(req)
                        ),
                        HttpStatus.CREATED
                )
        );
    }

    @Operation(summary = "Approve user", description = "Approve user by id")
    @ApiResponses(value = {
            @ApiResponse(code = 202, message = "User was approved successfully"),
            @ApiResponse(code = 404, message = "User could not be approved"),
            @ApiResponse(code = 401, message = "Authentication required"),
            @ApiResponse(code = 403, message = "Access denied")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/approve/{userId}")
    public ResponseEntity<CustomApiResponse> approveById(@PathVariable int userId) {
        return this.run(() -> {
            this.userService.approveUserById(userId);

            return ResponseFactory.success(
                    Map.of(
                            "message", "User with id: " + userId + " has been approved successfully."
                    ),
                    HttpStatus.ACCEPTED
            );
        });
    }

    @Operation(summary = "Get unapproved users", description = "Get all unapproved users")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Users were found successfully"),
            @ApiResponse(code = 404, message = "Users could not be found"),
            @ApiResponse(code = 401, message = "Authentication required"),
            @ApiResponse(code = 403, message = "Access denied")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/unapproved")
    public ResponseEntity<CustomApiResponse> getAllUnapproved() {
        return this.run(() ->
                ResponseFactory.success(
                        this.userService.findAllNotApproved()
                                .stream()
                                .map(UserMapper::fromEntityToResponse)
                                .toList()
                )
        );
    }

    @Operation(summary = "Forgot password", description = "Request password change by email")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Request was sent successfully"),
            @ApiResponse(code = 404, message = "Request could not be sent"),
    })
    @PermitAll
    @PostMapping("/forgot_password")
    public ResponseEntity<CustomApiResponse> forgotPassword(
            @Valid
            @RequestBody
            ForgotPasswordUserRequestDto req
    ) {
        return this.run(() ->
                ResponseFactory.success(
                        this.userService
                                .sendUniqueCodeForPasswordReset(req)
                )
        );
    }

    @Operation(summary = "Reset password", description = "Reset password by code")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Password was reset successfully"),
            @ApiResponse(code = 404, message = "Password could not be reset"),
            @ApiResponse(code = 410, message = "Password could not be reset - invalid code"),
    })
    @PermitAll
    @PostMapping("/reset")
    public ResponseEntity<CustomApiResponse> resetPassword(
            @RequestParam String code,
            @Valid
            @RequestBody
            ResetUserRequestDto req
    ) {
        return this.run(() -> {
            this.userService.resetPassword(code, req);

            return ResponseFactory.success(
                    Map.of(
                            "message", "Password has been successfully changed."
                    )
            );
        });
    }

    @Operation(summary = "Login user", description = "Login user by getting a jwt token")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User logged in successfully"),
            @ApiResponse(code = 404, message = "Could could not be logged in"),
            @ApiResponse(code = 410, message = "Failed to login too many times")
    })
    @PermitAll
    @PostMapping("/login")
    public ResponseEntity<CustomApiResponse> login(
            @Valid
            @RequestBody
            AuthenticateUserRequestDto req
    ) {
        return this.run(() ->
                ResponseFactory.success(
                        Map.of(
                                "token", this.userService.authenticate(req)
                        )
                )
        );
    }

    @Operation(summary = "Confirm email", description = "Confirm email by id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Email was confirmed successfully"),
            @ApiResponse(code = 410, message = "Email could not be confirmed - code was incorrect"),
    })
    @PermitAll
    @GetMapping("/confirm")
    public ResponseEntity<CustomApiResponse> confirm(
            @RequestParam String code
    ) {
        return this.run(() -> {
            this.userService.confirmEmail(code);

            return ResponseFactory.success(
                    Map.of(
                            "message", "Email has been successfully confirmed."
                    )
            );
        });
    }
}
