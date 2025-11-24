package com.leverx.trugame.controllers;

import com.leverx.trugame.entities.GameEntity;
import com.leverx.trugame.mappers.GameMapper;
import com.leverx.trugame.requests.games.CreateGameRequestDto;
import com.leverx.trugame.requests.games.UpdateGameRequestDto;
import com.leverx.trugame.services.GameService;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.PermitAll;
import javax.validation.Valid;
import java.util.Map;

@RequiredArgsConstructor

@RestController
@RequestMapping("/games")
@Api(value = "Game Management", tags = {"Game Controller"})
public class GameController extends BaseController {

    private final GameService gameService;

    @Operation(summary = "Update game", description = "Update game by id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Game was updated successfully"),
            @ApiResponse(code = 404, message = "Game could not be updated"),
            @ApiResponse(code = 401, message = "Authentication required")
    })
    @PreAuthorize("hasAnyRole('SELLER', 'ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<CustomApiResponse> updateGame(
            @PathVariable int id,
            @Valid @RequestBody UpdateGameRequestDto req
    ) {
        return this.run(() -> {
            GameEntity game = this.gameService.updateGameById(id, req);

            return ResponseFactory.success(
                    GameMapper.fromEntityToResponse(game)
            );
        });
    }

    @Operation(summary = "Create game", description = "Create game by user")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Game was created successfully"),
            @ApiResponse(code = 404, message = "Game could not be created"),
            @ApiResponse(code = 401, message = "Authentication required")
    })
    @PreAuthorize("hasAnyRole('SELLER', 'ADMIN')")
    @PostMapping
    public ResponseEntity<CustomApiResponse> createGame(
            @Valid
            @RequestBody
            CreateGameRequestDto req
    ) {
        return this.run(() -> {
            GameEntity game = this.gameService.createGame(req);

            return ResponseFactory.success(
                    GameMapper.fromEntityToResponse(game),
                    HttpStatus.CREATED
            );
        });
    }

    @Operation(summary = "Get games", description = "Get all games")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Games were found successfully"),
            @ApiResponse(code = 404, message = "Games could not be found"),
    })
    @PermitAll
    @GetMapping
    public ResponseEntity<CustomApiResponse> getGames() {
        return this.run(() ->
                ResponseFactory.success(
                        this.gameService.findAllGames()
                                .stream()
                                .map(GameMapper::fromEntityToResponse)
                                .toList()
                )
        );
    }

    @Operation(summary = "Get game", description = "Get game by id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Game was found successfully"),
            @ApiResponse(code = 404, message = "Game could not be found")
    })
    @PermitAll
    @GetMapping("/{gameId}")
    public ResponseEntity<CustomApiResponse> getGameById(@PathVariable int gameId) {
        return this.run(() ->
                ResponseFactory.success(
                        GameMapper.fromEntityToResponse(
                                this.gameService.findGameById(gameId)
                        )
                )
        );
    }

    @Operation(summary = "Delete game", description = "Delete game by id")
    @ApiResponses(value = {
            @ApiResponse(code = 202, message = "Game was deleted successfully"),
            @ApiResponse(code = 404, message = "Game could not be deleted"),
            @ApiResponse(code = 401, message = "Authentication required"),
    })
    @PreAuthorize("hasAnyRole('SELLER', 'ADMIN')")
    @DeleteMapping("/{gameId}")
    public ResponseEntity<CustomApiResponse> deleteGame(@PathVariable int gameId) {
        return this.run(() -> {
            this.gameService.deleteGameById(gameId);

            return ResponseFactory.success(
                    Map.of(
                            "message", "Game with id:" + gameId + " deleted successfully "
                    ),
                    HttpStatus.ACCEPTED
            );
        });
    }
}
