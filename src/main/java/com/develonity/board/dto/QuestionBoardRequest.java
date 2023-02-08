package com.develonity.board.dto;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class QuestionBoardRequest {

  private String title;
  private String content;
  private String category;
  private int point;

}
