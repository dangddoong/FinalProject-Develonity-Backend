package com.develonity.comment.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
//  private User user;
//
//  @ManyToOne(fetch = FetchType.LAZY)
//  private Board board;

}
