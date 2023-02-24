package com.develonity.board.dto;

import com.develonity.board.entity.CommunityCategory;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@NoArgsConstructor(force = true, access = AccessLevel.PROTECTED)
@RequiredArgsConstructor
public class CommunityBoardRequest {

  private final String title;
  private final String content;
  private final CommunityCategory communityCategory;
}