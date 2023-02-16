package com.develonity.comment.dto;

import com.develonity.comment.entity.ReplyComment;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ReplyCommentResponse {

  private final Long id;
  private final String content;
  private final String nickName;
  private final LocalDateTime createdAt;
  private final LocalDateTime lastModifiedAt;


  public ReplyCommentResponse(ReplyComment replyComment) {
    this.id = replyComment.getId();
    this.content = replyComment.getContent();
    this.nickName = replyComment.getNickName();
    this.createdAt = replyComment.getCreatedDate();
    this.lastModifiedAt = replyComment.getLastModifiedDate();
  }
}
