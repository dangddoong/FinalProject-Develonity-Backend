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
  private final String nickname;
  private final LocalDateTime createdAt;
  private final LocalDateTime lastModifiedAt;


  public ReplyCommentResponse(ReplyComment replyComment, String nickname) {
    this.id = replyComment.getId();
    this.content = replyComment.getContent();
    this.nickname = nickname;
    this.createdAt = replyComment.getCreatedDate();
    this.lastModifiedAt = replyComment.getLastModifiedDate();
  }
}
