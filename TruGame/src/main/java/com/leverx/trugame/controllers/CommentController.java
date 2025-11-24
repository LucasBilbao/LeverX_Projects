package com.leverx.trugame.controllers;

import com.leverx.trugame.entities.CommentEntity;
import com.leverx.trugame.mappers.CommentMapper;
import com.leverx.trugame.requests.comments.CreateCommentRequestDto;
import com.leverx.trugame.requests.comments.UpdateCommentRequestDto;
import com.leverx.trugame.services.CommentService;
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
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@RequiredArgsConstructor

@RestController
@RequestMapping("/comments")
@Api(value = "Comment Management", tags = {"Comment Controller"})
public class CommentController extends BaseController {

    private final CommentService commentService;

    @Operation(summary = "Get comment", description = "Get comment by id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Comment was found successfully"),
            @ApiResponse(code = 404, message = "Comment could not be found"),
    })
    @PermitAll
    @GetMapping("/{commentId}")
    public ResponseEntity<CustomApiResponse> getByCommentId(@PathVariable int commentId) {
        return this.run(() -> {
            CommentEntity comment = this.commentService.findCommentById(commentId);

            return ResponseFactory.success(
                    CommentMapper.fromEntityToResponse(comment)
            );
        });
    }

    @Operation(summary = "Get comment by game", description = "Get comment by game id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Comment was found successfully"),
            @ApiResponse(code = 404, message = "Comment could not be found"),
    })
    @PermitAll
    @GetMapping("/game/{gameId}")
    public ResponseEntity<CustomApiResponse> getByGameId(@PathVariable int gameId) {
        return this.getAllById(gameId, this.commentService::findCommentsByGameId);
    }

    @Operation(summary = "Get comment by author", description = "Get comment by author id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Comment was found successfully"),
            @ApiResponse(code = 404, message = "Comment could not be found"),
    })
    @PermitAll
    @GetMapping("/author/{authorId}")
    public ResponseEntity<CustomApiResponse> getByAuthorId(@PathVariable int authorId) {
        return this.getAllById(authorId, this.commentService::findCommentsByAuthorId);
    }

    @Operation(summary = "Get unapproved comments", description = "Get comments that have not been approved")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Comments were found successfully"),
            @ApiResponse(code = 404, message = "Comments could not be found"),
            @ApiResponse(code = 401, message = "Authentication required"),
            @ApiResponse(code = 403, message = "Access denied")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/unapproved")
    public ResponseEntity<CustomApiResponse> getAllUnapproved() {
        return this.run(() ->
                ResponseFactory.success(
                        this.commentService.findAllNotApproved()
                                .stream()
                                .map(CommentMapper::fromEntityToResponse)
                                .toList()
                )
        );
    }

    @Operation(summary = "Create new comment", description = "Create new comment for a game")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Comment was created successfully"),
            @ApiResponse(code = 404, message = "Comment could not be created"),
    })
    @PermitAll
    @PostMapping("/game/{gameId}")
    public ResponseEntity<CustomApiResponse> postComment(
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

    @Operation(summary = "Update comment", description = "Update comment by id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Comment was updated successfully"),
            @ApiResponse(code = 404, message = "Comment could not be updated"),
            @ApiResponse(code = 401, message = "Authentication required")
    })
    @PreAuthorize("hasAnyRole('SELLER', 'ADMIN')")
    @PutMapping("/{commentId}")
    public ResponseEntity<CustomApiResponse> putComment(
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

    @Operation(summary = "Delete comment", description = "Delete comment by id")
    @ApiResponses(value = {
            @ApiResponse(code = 202, message = "Comment was deleted successfully"),
            @ApiResponse(code = 404, message = "Comment could not be deleted"),
            @ApiResponse(code = 401, message = "Authentication required")
    })
    @PreAuthorize("hasAnyRole('SELLER', 'ADMIN')")
    @DeleteMapping("/{commentId}")
    public ResponseEntity<CustomApiResponse> deleteComment(@PathVariable int commentId) {
        return this.run(() -> {
            this.commentService.deleteCommentById(commentId);

            return ResponseFactory.success(
                    Map.of(
                            "message", "Comment with id: " + commentId + " was deleted successfully"
                    ),
                    HttpStatus.ACCEPTED
            );
        });
    }

    @Operation(summary = "Approve comment", description = "Approve comment by id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Comment was approved successfully"),
            @ApiResponse(code = 404, message = "Comment could not be approved"),
            @ApiResponse(code = 401, message = "Authentication required"),
            @ApiResponse(code = 403, message = "Access denied")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/approve/{commentId}")
    public ResponseEntity<CustomApiResponse> approveByGameId(@PathVariable int commentId) {
        return this.run(() -> {
            this.commentService.approveCommentById(commentId);

            return ResponseFactory.success(
                    Map.of(
                            "message", "Comment with id: " + commentId + " has been approved successfully."
                    ),
                    HttpStatus.ACCEPTED
            );
        });
    }

    private ResponseEntity<CustomApiResponse> getAllById(int id, Function<Integer, List<CommentEntity>> func) {
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
