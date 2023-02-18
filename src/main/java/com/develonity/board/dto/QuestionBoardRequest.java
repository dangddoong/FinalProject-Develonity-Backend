package com.develonity.board.dto;


import com.develonity.board.entity.QuestionCategory;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@NoArgsConstructor(force = true, access = AccessLevel.PROTECTED)
@RequiredArgsConstructor
public class QuestionBoardRequest {

  private final String title;
  private final String content;
  private final int point;
  private final String imageUrl;
  private final QuestionCategory questionCategory;

}