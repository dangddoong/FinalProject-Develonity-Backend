package com.develonity.comment.entity;

import javax.persistence.Column;
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

//  @ManyToOne
//  @Column(name = "user_id")
//  private User user;

  @ManyToOne
  @Column(name = "comment_id")
  private Comment comment;

}
