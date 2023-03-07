package com.develonity.board.dto;


import com.develonity.board.entity.QuestionCategory;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.NumberFormat;

@Getter
@NoArgsConstructor(force = true, access = AccessLevel.PROTECTED)
@RequiredArgsConstructor
public class QuestionBoardRequest {

  private final String title;
  private final String content;

  @Max(value = 1000)
  @Min(value = 0)
  private final int point;
  private final QuestionCategory questionCategory;

}