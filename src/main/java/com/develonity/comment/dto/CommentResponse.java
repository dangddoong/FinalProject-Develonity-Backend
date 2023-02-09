package com.develonity.comment.dto;

import com.develonity.comment.entity.Comment;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class CommentResponse {

  private Long id;

  private String username;

  private String content;

  private Long commentLike;

  private int point;

  private LocalDateTime createdAt;

  private LocalDateTime modifiedAt;


  public CommentResponse(Comment comment) {
    this.id = comment.getId();
    this.username = comment.getUsername();
    this.content = comment.getContent();
    this.commentLike = comment.getCommentlikes();
    this.point = comment.getPoint();
    this.createdAt = comment.getCreatedDate();
  }

  public static CommentResponse toCommentResponseDto(final Comment comment) {
    return CommentResponse.builder()
        .id(comment.getId())
        .username(comment.getUsername())
        .content(comment.getContent())
        .point(comment.getPoint())
        .createdAt(comment.getCreatedDate())
        .build();
  }

}
