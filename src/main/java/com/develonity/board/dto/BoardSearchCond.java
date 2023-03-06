package com.develonity.board.dto;

import com.develonity.board.entity.BoardSort;
import com.develonity.board.entity.BoardStatus;
import com.develonity.board.entity.CommunityCategory;
import com.develonity.board.entity.QuestionCategory;
import com.develonity.board.entity.SortDirection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class BoardSearchCond {
  private String title;
  private String content;
  private String nickname;
//  private String sort; // sort=like , sort=new ==>
  private BoardSort boardSort;

  private SortDirection sortDirection;


}
