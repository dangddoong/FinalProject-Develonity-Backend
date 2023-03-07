package com.develonity.board.dto;

import com.develonity.board.entity.CommunityCategory;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class CommunityBoardSearchCond extends BoardSearchCond{

  private CommunityCategory communityCategory;

}
