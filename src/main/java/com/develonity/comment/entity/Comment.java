package com.develonity.comment.entity;

import com.develonity.board.entity.Board;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Comment {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long Id;

  @Column(nullable = false)
  public String username;

  @Column(nullable = false)
  public String content;

  @Column(nullable = false)
  public boolean adoptStatus;

//  @ManyToOne(fetch = FetchType.LAZY)
//  @Column(name = "user_id", nullable = false)
//  private User user;
//
//  @ManyToOne(fetch = FetchType.LAZY)
//  @Column(name = "board_id", nullable = false)
//  private Board board;

}
