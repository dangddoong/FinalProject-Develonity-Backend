package com.develonity.board.dto;

import com.develonity.board.entity.BoardStatus;
import com.develonity.board.entity.QuestionCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionBoardSearchCond {


  private String title;
  private String content;
  private QuestionCategory questionCategory;
  private BoardStatus boardStatus;

  private String nickname;
  private String sort; // sort=like , sort=new ==>

}
