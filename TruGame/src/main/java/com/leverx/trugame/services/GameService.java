package com.leverx.trugame.services;

import com.leverx.trugame.entities.GameEntity;
import com.leverx.trugame.entities.UserEntity;
import com.leverx.trugame.exceptions.NotFoundException;
import com.leverx.trugame.mappers.GameMapper;
import com.leverx.trugame.repositories.GameRepository;
import com.leverx.trugame.requests.games.CreateGameRequestDto;
import com.leverx.trugame.requests.games.UpdateGameRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor

@Service
public class GameService {

    private final GameRepository repo;
    private final UserService userService;

    public List<GameEntity> findAllGames() {
        return this.repo.findAll();
    }

    public GameEntity findGameById(int id) throws NotFoundException {
        return this.repo.findById(id)
                .orElseThrow(() -> new NotFoundException("Game with id " + id + " not found."));
    }

    public void deleteGameById(int id) throws NotFoundException {
        if (!this.existsById(id)) {
            throw new NotFoundException("Game with id: " + id + " not found.");
        }

        this.repo.deleteById(id);
    }

    public GameEntity createGame(CreateGameRequestDto req) throws NotFoundException {
        UserEntity user = this.userService.findUserById(req.getUserId());

        GameEntity game = GameMapper.fromRequestToEntity(req, user);

        this.repo.save(game);
        return game;
    }

    public GameEntity updateGameById(int id, UpdateGameRequestDto req) throws NotFoundException {
        GameEntity game = this.repo.findById(id).orElseThrow(() -> new NotFoundException("Game with id " + id + " " +
                "not found."));

        String title = !req.getTitle().isBlank() ? req.getTitle() : game.getTitle();
        String text = !req.getText().isBlank() ? req.getText() : game.getText();

        game.setTitle(title);
        game.setText(text);

        this.repo.save(game);
        return game;
    }

    public boolean existsById(int id) {
        return this.repo.existsById(id);
    }
}
