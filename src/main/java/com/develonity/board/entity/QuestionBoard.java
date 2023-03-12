package com.develonity.board.entity;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorValue("QuestionBoard")
public class QuestionBoard extends Board {

  @Max(value = 1000)
  @Min(value = 0)
  @Column
  private int prizePoint = 0;
  @Column
  @Enumerated(EnumType.STRING)
  private BoardStatus status = BoardStatus.NOT_ADOPTED;
  @Column
  @Enumerated(EnumType.STRING)
  private QuestionCategory questionCategory;


  @Builder
  public QuestionBoard(Long userId, String title, String content,
      int prizePoint, QuestionCategory questionCategory) {
    super(userId, title, content);
    this.prizePoint = prizePoint;
    this.questionCategory = questionCategory;
  }

  public void updateBoard(String title, String content, QuestionCategory questionCategory) {
    super.updateBoard(title, content);
    this.questionCategory = questionCategory;
  }

  public void changeStatus() {
    this.status = BoardStatus.ADOPTED;
  }


  public boolean isAlreadyAdopted() {
    return this.status.equals(BoardStatus.ADOPTED);
  }
}
