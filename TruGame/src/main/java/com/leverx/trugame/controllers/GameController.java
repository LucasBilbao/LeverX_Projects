package com.leverx.trugame.controllers;

import com.leverx.trugame.entities.GameEntity;
import com.leverx.trugame.mappers.GameMapper;
import com.leverx.trugame.requests.games.CreateGameRequestDto;
import com.leverx.trugame.requests.games.UpdateGameRequestDto;
import com.leverx.trugame.services.GameService;
import com.leverx.trugame.web.ResponseFactory;
import com.leverx.trugame.web.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RequiredArgsConstructor

@RestController
@RequestMapping("/games")
public class GameController extends BaseController {

    private final GameService gameService;

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateGame(
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

    @PostMapping
    public ResponseEntity<ApiResponse> createGame(
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

    @GetMapping
    public ResponseEntity<ApiResponse> getGames() {
        return this.run(() ->
                ResponseFactory.success(
                        this.gameService.findAllGames()
                                .stream()
                                .map(GameMapper::fromEntityToResponse)
                                .toList()
                )
        );
    }

    @GetMapping("/{gameId}")
    public ResponseEntity<ApiResponse> getGameById(@PathVariable int gameId) {
        return this.run(() ->
                ResponseFactory.success(
                        GameMapper.fromEntityToResponse(
                                this.gameService.findGameById(gameId)
                        )
                )
        );
    }

    @DeleteMapping("/{gameId}")
    public ResponseEntity<ApiResponse> deleteGame(@PathVariable int gameId) {
        return this.run(() -> {
            this.gameService.deleteGameById(gameId);

            return ResponseFactory.success(
                    "Game with id: " + gameId + " deleted successfully",
                    HttpStatus.ACCEPTED
            );
        });
    }
}
