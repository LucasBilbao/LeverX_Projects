package com.leverx.trugame.services;

import com.leverx.trugame.entities.CommentEntity;
import com.leverx.trugame.entities.GameEntity;
import com.leverx.trugame.entities.UserEntity;
import com.leverx.trugame.exceptions.NotFoundException;
import com.leverx.trugame.mappers.CommentMapper;
import com.leverx.trugame.repositories.CommentRepository;
import com.leverx.trugame.requests.comments.CreateCommentRequestDto;
import com.leverx.trugame.requests.comments.UpdateCommentRequestDto;
import com.leverx.trugame.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor

@Service
public class CommentService {

    private final CommentRepository repo;
    private final UserService userService;
    private final GameService gameService;

    @Transactional(readOnly = true)
    public CommentEntity findCommentById(int commentId) throws NotFoundException {
        return this.repo.findById(commentId)
                .orElseThrow(
                        () -> new NotFoundException("Comment with id: " + commentId + " not found.")
                );
    }

    @Transactional(readOnly = true)
    public List<CommentEntity> findCommentsByGameId(int gameId) throws NotFoundException {
        if (!this.gameService.existsById(gameId)) {
            throw new NotFoundException("Game with id: " + gameId + " not found.");
        }

        return this.repo.findAllByGame_Id(gameId);
    }

    @Transactional(readOnly = true)
    public List<CommentEntity> findCommentsByAuthorId(int authorId) throws NotFoundException {
        if (!this.userService.existsById(authorId)) {
            throw new NotFoundException("Author with id: " + authorId + " not found.");
        }

        return this.repo.findAllByAuthor_Id(authorId);
    }

    @Transactional(readOnly = true)
    public List<CommentEntity> findAllNotApproved() {
        return this.repo.findAllNotApproved();
    }

    @Transactional
    public CommentEntity saveComment(int gameId, CreateCommentRequestDto req) throws NotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        UserEntity user;

        if (
                authentication.getPrincipal() instanceof String &&
                        authentication.getPrincipal().equals("anonymousUser")
        ) {
            user = this.userService.saveNewAnonymousUser();

        } else {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

            int id = userDetails.getId();
            user = this.userService.findUserById(id);
        }

        GameEntity game = this.gameService.findGameById(gameId);
        CommentEntity comment = CommentMapper.fromRequestToEntity(req, user, game);

        this.repo.save(comment);

        return comment;
    }

    @Transactional
    public CommentEntity updateCommentById(
            int commentId,
            UpdateCommentRequestDto req
    ) throws NotFoundException {
        CommentEntity comment = this.findCommentById(commentId);

        String message = req.getMessage().isBlank() ? comment.getMessage() : req.getMessage();
        short rating = req.getRating() == null ? comment.getRating() : req.getRating();

        comment.setMessage(message);
        comment.setRating(rating);

        this.repo.save(comment);

        return comment;
    }

    @Transactional
    public void deleteCommentById(int commentId) throws NotFoundException {
        if (!this.existsById(commentId)) {
            throw new NotFoundException("Comment with id: " + commentId + " not found.");
        }

        this.repo.deleteById(commentId);
    }

    @Transactional
    public void approveCommentById(int commentId) throws NotFoundException {
        if (!this.existsById(commentId)) {
            throw new NotFoundException("Comment with id: " + commentId + " not found.");
        }

        CommentEntity comment = this.findCommentById(commentId);
        comment.setApproved(true);
        this.repo.save(comment);
    }

    public boolean existsById(int id) {
        return this.repo.existsById(id);
    }
}
