package com.develonity.comment.dto;

import com.develonity.comment.entity.Comment;
import com.develonity.comment.entity.CommentStatus;
import com.develonity.comment.entity.ReplyComment;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
@AllArgsConstructor
public class CommentResponse {

  private final Long id;
  private final String nickname;
  private final String content;
  private final LocalDateTime createdAt;
  private final LocalDateTime lastModifiedAt;
  private Boolean hasLike;
  private final CommentStatus commentStatus;
  private final long commentLike;
  private List<ReplyCommentResponse> replyCommentList;


  public CommentResponse(Comment comment, String nickname, long commentLike) {
    List<ReplyCommentResponse> list = new ArrayList<>();
    this.id = comment.getId();
    this.nickname = nickname;
    this.content = comment.getContent();
    this.commentLike = commentLike;
    this.createdAt = comment.getCreatedDate();
    this.lastModifiedAt = comment.getLastModifiedDate();
    for (ReplyComment replyComment : comment.getReplyCommentList) {
      list.add(new ReplyCommentResponse(replyComment, nickname));
    }
    this.commentStatus = comment.getCommentStatus();
    this.replyCommentList = list;
  }

  public CommentResponse(Comment comment, String nickname, long commentLike, Boolean hasLike,
      List<ReplyCommentResponse> replyCommentResponses) {

    this.id = comment.getId();
    this.nickname = nickname;
    this.content = comment.getContent();
    this.commentLike = commentLike;
    this.createdAt = comment.getCreatedDate();
    this.lastModifiedAt = comment.getLastModifiedDate();
    this.hasLike = hasLike;
    this.commentStatus = comment.getCommentStatus();
    this.replyCommentList = replyCommentResponses;
  }

  // Querydsl 전용
  public CommentResponse(
      Long id,
      String nickname,
      String content,
      LocalDateTime createdAt,
      LocalDateTime lastModifiedAt,
      CommentStatus commentStatus,
      long commentLike
//      List<ReplyCommentResponse> replyCommentList
  ) {

    this.id = id;
    this.nickname = nickname;
    this.content = content;
    this.createdAt = createdAt;
    this.lastModifiedAt = lastModifiedAt;
    this.commentStatus = commentStatus;
    this.commentLike = commentLike;
//    this.replyCommentList = replyCommentList;
  }
}
