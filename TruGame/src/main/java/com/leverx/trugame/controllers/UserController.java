package com.leverx.trugame.controllers;

import com.leverx.trugame.mappers.UserMapper;
import com.leverx.trugame.requests.users.AuthenticateUserRequestDto;
import com.leverx.trugame.requests.users.ForgotPasswordUserRequestDto;
import com.leverx.trugame.requests.users.RegisterUserRequestDto;
import com.leverx.trugame.requests.users.ResetUserRequestDto;
import com.leverx.trugame.services.UserService;
import com.leverx.trugame.web.ResponseFactory;
import com.leverx.trugame.web.dto.ApiResponse;
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
public class UserController extends BaseController {

    private final UserService userService;

    @PermitAll
    @PostMapping("/register")
    public ResponseEntity<ApiResponse> registerUser(
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

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/approve/{userId}")
    public ResponseEntity<ApiResponse> approveById(@PathVariable int userId) {
        return this.run(() -> {
            this.userService.approveUserById(userId);

            return ResponseFactory.success(
                    "User with id: " + userId + " has been approved successfully.",
                    HttpStatus.ACCEPTED
            );
        });
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/unapproved")
    public ResponseEntity<ApiResponse> getAllUnapproved() {
        return this.run(() ->
                ResponseFactory.success(
                        this.userService.findAllNotApproved()
                                .stream()
                                .map(UserMapper::fromEntityToResponse)
                                .toList()
                )
        );
    }

    @PermitAll
    @PostMapping("/forgot_password")
    public ResponseEntity<ApiResponse> forgotPassword(
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

    @PermitAll
    @PostMapping("/reset")
    public ResponseEntity<ApiResponse> resetPassword(
            @RequestParam String code,
            @Valid
            @RequestBody
            ResetUserRequestDto req
    ) {
        return this.run(() -> {
            this.userService.resetPassword(code, req);

            return ResponseFactory.success(
                    "Password has been successfully changed."
            );
        });
    }

    @PermitAll
    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(
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

    @PermitAll
    @GetMapping("/confirm")
    public ResponseEntity<ApiResponse> confirm(
            @RequestParam String code
    ) {
        return this.run(() -> {
            this.userService.confirmEmail(code);

            return ResponseFactory.success(
                    "Email has been successfully confirmed."
            );
        });
    }
}
