package com.develonity.comment.entity;

import com.develonity.comment.dto.ReplyCommentRequest;
import com.develonity.user.entity.TimeStamp;
import com.develonity.user.entity.User;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReplyComment extends TimeStamp {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String content;

  @JoinColumn(name = "USER_ID")
  private Long userId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "COMMENT_ID")
  private Comment comment;


  public ReplyComment(User user, ReplyCommentRequest request, Comment comment) {
    this.content = request.getContent();
    this.comment = comment;
    this.userId = user.getId();
    comment.getReplyCommentList.add(this);
  }

  public void updateReplyComment(String content) {
    this.content = content;
  }
}
