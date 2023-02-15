package com.develonity.board.entity;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorValue("QuestionBoard")
public class QuestionBoard extends Board {

  @Column
  private int prizePoint;
  @Column
  @Enumerated(EnumType.STRING)
  private BoardStatus status = BoardStatus.NOT_ADOPTED;
  @Column
  @Enumerated(EnumType.STRING)
  private SubCategory subCategory;


  @Builder
  public QuestionBoard(Long userId, String title, String content, Category category,
      int prizePoint, SubCategory subCategory) {
    super(userId, title, content, category);
    this.prizePoint = prizePoint;
    this.subCategory = subCategory;
  }


  public void changeStatus() {
    this.status = BoardStatus.ADOPTED;
  }

  @Override
  public boolean isWriter(Long id) {
    return super.isWriter(id);
  }
}
