package com.develonity.board.dto;

import com.develonity.board.entity.Category;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class QuestionBoardRequest {

  private String title;
  private String content;
  private Category category;
  private int point;
  private String imageUrl;

}
