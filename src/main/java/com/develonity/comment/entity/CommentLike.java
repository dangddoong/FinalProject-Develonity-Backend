package com.develonity.comment.entity;

import com.develonity.user.entity.User;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class CommentLike {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long Id;

  @ManyToOne
  private User user;

  @ManyToOne
  private Comment comment;

  public CommentLike(User user, Comment comment) {
    this.user = user;
    this.comment = comment;
  }
}
