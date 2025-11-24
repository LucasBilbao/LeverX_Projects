package com.leverx.trugame.mappers;

import com.leverx.trugame.entities.CommentEntity;
import com.leverx.trugame.entities.GameEntity;
import com.leverx.trugame.entities.UserEntity;
import com.leverx.trugame.requests.comments.CreateCommentRequestDto;
import com.leverx.trugame.responses.CommentResponseDto;

public class CommentMapper {

    public static CommentResponseDto fromEntityToResponse(CommentEntity comment) {
        return CommentResponseDto.builder()
                .id(comment.getId())
                .message(comment.getMessage())
                .rating(comment.getRating())
                .isApproved(comment.isApproved())
                .authorId(comment.getAuthor().getId())
                .gameId(comment.getGame().getId())
                .createdAt(comment.getCreatedAt())
                .build();
    }

    public static CommentEntity fromRequestToEntity(CreateCommentRequestDto comment, UserEntity author, GameEntity game) {
        return CommentEntity.builder()
                .message(comment.getMessage())
                .rating(comment.getRating())
                .author(author)
                .game(game)
                .build();
    }

}
