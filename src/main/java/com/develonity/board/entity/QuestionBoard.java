package com.develonity.board.entity;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

@Entity
@Getter
@DynamicInsert
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorValue("QuestionBoard")
public class QuestionBoard extends Board {

  @Column(nullable = false)
  private int prizePoint;

  @Column(nullable = false)
  @ColumnDefault("'답변 미채택 게시글'")
  private String status;


  public void changeStatus() {
    this.status = "답변 채택 게시글";
  }

}