package com.develonity.comment.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentLike {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "COMMENT_LIKE_ID")
  private Long id;

  @JoinColumn(name = "USER_ID")
  private Long userId;

  @JoinColumn(name = "COMMENT_ID")
  private Long commentId;

  public CommentLike(Long userId, Long commentId) {
    this.userId = userId;
    this.commentId = commentId;
  }
}

