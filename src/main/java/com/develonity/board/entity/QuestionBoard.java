package com.develonity.board.entity;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorValue("QuestionBoard")
public class QuestionBoard extends Board {

  @Column(nullable = false)
  private int prizePoint;
  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private BoardStatus status = BoardStatus.NOT_ADOPTED;

  public QuestionBoard(Long userId, String title, String content, Category category,
      String imageUrl, int prizePoint) {
    super(userId, title, content, category, imageUrl);
    this.prizePoint = prizePoint;
  }


  public void changeStatus() {
    this.status = BoardStatus.ADOPTED;
  }


}