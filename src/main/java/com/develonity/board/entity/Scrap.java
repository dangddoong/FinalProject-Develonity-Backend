package com.develonity.board.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Scrap {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "SCRAP_ID")
  private Long id;

  @JoinColumn(name = "USER_ID")
  private Long userId;
  @JoinColumn(name = "BOARD_ID")
  private Long boardId;

  public Scrap(Long userId, Long boardId) {
    this.userId = userId;
    this.boardId = boardId;
  }

}
