package com.leverx.trugame.controllers;

import com.leverx.trugame.entities.CommentEntity;
import com.leverx.trugame.mappers.CommentMapper;
import com.leverx.trugame.requests.comments.CreateCommentRequestDto;
import com.leverx.trugame.requests.comments.UpdateCommentRequestDto;
import com.leverx.trugame.services.CommentService;
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
import java.util.List;
import java.util.function.Function;

@RequiredArgsConstructor

@RestController
@RequestMapping("/comments")
public class CommentController extends BaseController {

    private final CommentService commentService;

    @GetMapping("/{commentId}")
    public ResponseEntity<ApiResponse> getByCommentId(@PathVariable int commentId) {
        return this.run(() -> {
            CommentEntity comment = this.commentService.findCommentById(commentId);

            return ResponseFactory.success(
                    CommentMapper.fromEntityToResponse(comment)
            );
        });
    }

    @GetMapping("/game/{gameId}")
    public ResponseEntity<ApiResponse> getByGameId(@PathVariable int gameId) {
        return this.getAllById(gameId, this.commentService::findCommentsByGameId);
    }

    @GetMapping("/author/{authorId}")
    public ResponseEntity<ApiResponse> getByAuthorId(@PathVariable int authorId) {
        return this.getAllById(authorId, this.commentService::findCommentsByAuthorId);
    }

    @GetMapping("/unapproved")
    public ResponseEntity<ApiResponse> getAllUnapproved() {
        return this.run(() ->
                ResponseFactory.success(
                        this.commentService.findAllNotApproved()
                                .stream()
                                .map(CommentMapper::fromEntityToResponse)
                                .toList()
                )
        );
    }

    @PostMapping("/game/{gameId}")
    public ResponseEntity<ApiResponse> postComment(
            @PathVariable int gameId,
            @Valid
            @RequestBody
            CreateCommentRequestDto req
    ) {
        return this.run(() ->
                ResponseFactory.success(
                        CommentMapper.fromEntityToResponse(
                                this.commentService.saveComment(gameId, req)
                        ),
                        HttpStatus.CREATED
                )
        );
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<ApiResponse> putComment(
            @PathVariable int commentId,
            @Valid
            @RequestBody
            UpdateCommentRequestDto req
    ) {
        return this.run(() ->
                ResponseFactory.success(
                        CommentMapper.fromEntityToResponse(
                                this.commentService.updateCommentById(commentId, req)
                        )
                )
        );
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<ApiResponse> deleteComment(@PathVariable int commentId) {
        return this.run(() -> {
            this.commentService.deleteCommentById(commentId);

            return ResponseFactory.success(
                    "Comment with id: " + commentId + " was deleted successfully",
                    HttpStatus.ACCEPTED
            );
        });
    }

    @GetMapping("/approve/{commentId}")
    public ResponseEntity<ApiResponse> getApprovedByGameId(@PathVariable int commentId) {
        return this.run(() -> {
            this.commentService.approveCommentById(commentId);

            return ResponseFactory.success(
                    "Comment with id: " + commentId + " has been approved successfully.",
                    HttpStatus.ACCEPTED
            );
        });
    }

    private ResponseEntity<ApiResponse> getAllById(int id, Function<Integer, List<CommentEntity>> func) {
        return this.run(() ->
                ResponseFactory.success(
                        func.apply(id)
                                .stream()
                                .map(CommentMapper::fromEntityToResponse)
                                .toList()
                )
        );
    }
}
