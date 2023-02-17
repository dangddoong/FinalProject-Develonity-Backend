package com.develonity.board.entity;

import com.develonity.user.entity.TimeStamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Entity
public class BoardImage extends TimeStamp {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @ManyToOne
  @JoinColumn(name = "BOARD_ID")
  private Board board;
  @Column(nullable = false)
  private String imagePath;


  public BoardImage(String filePath, Board board) {
    this.imagePath = filePath;
    this.board = board;
  }
}
