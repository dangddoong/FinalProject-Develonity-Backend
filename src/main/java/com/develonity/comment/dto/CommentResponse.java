package com.develonity.comment.dto;

import com.develonity.comment.entity.Comment;
import com.develonity.comment.entity.ReplyComment;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class CommentResponse {

  private final Long id;

  private final String nickName;

  private final String content;

  private final int commentLike;

  private final int point;

  private final LocalDateTime createdAt;

  private final LocalDateTime lastModifiedAt;

  private List<ReplyCommentResponse> replyCommentList;


  public CommentResponse(Comment comment, int commentLike) {
    List<ReplyCommentResponse> list = new ArrayList<>();
    this.id = comment.getId();
    this.nickName = comment.getNickName();
    this.content = comment.getContent();
    this.commentLike = commentLike;
    this.point = comment.getPoint();
    this.createdAt = comment.getCreatedDate();
    this.lastModifiedAt = comment.getLastModifiedDate();
    for (ReplyComment replyComment : comment.getReplyCommentList) {
      list.add(new ReplyCommentResponse(replyComment));
    }
    this.replyCommentList = list;
  }

//  public static CommentResponse toCommentResponseDto(final Comment comment) {
//    return CommentResponse.builder()
//        .id(comment.getId())
//        .nickName(comment.getNickName())
//        .content(comment.getContent())
//        .commentLike(Math.toIntExact(comment.getId()))
//        .point(comment.getPoint())
//        .createdAt(comment.getCreatedDate())
//        .build();
//  }

}
