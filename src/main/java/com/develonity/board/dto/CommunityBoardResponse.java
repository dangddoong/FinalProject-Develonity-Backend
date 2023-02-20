package com.develonity.board.dto;

import com.develonity.board.entity.CommunityBoard;
import com.develonity.board.entity.CommunityCategory;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CommunityBoardResponse {

  private final Long id;
  private final String nickname;
  private final CommunityCategory communityCategory;
  private final String title;
  private final String content;
  private final int boardLike;
  private final LocalDateTime createdAt;
  private final LocalDateTime lastModifiedAt;
  private final List<String> imagePaths;
  private final Boolean hasLike; //로그인한 유저가 좋아요 눌렀는지 여부


  public CommunityBoardResponse(CommunityBoard communityBoard, String nickname, int boardLike,
      Boolean isLike, List<String> imagePaths) {
    this.id = communityBoard.getId();
    this.nickname = nickname;
    this.communityCategory = communityBoard.getCommunityCategory();
    this.title = communityBoard.getTitle();
    this.content = communityBoard.getContent();
    this.boardLike = boardLike;
    this.createdAt = communityBoard.getCreatedDate();
    this.lastModifiedAt = communityBoard.getLastModifiedDate();
    this.hasLike = isLike;
    this.imagePaths = imagePaths;


  }

  public static CommunityBoardResponse toCommunityBoardResponse(CommunityBoard communityBoard,
      String nickname) {
    return CommunityBoardResponse.builder()
        .id(communityBoard.getId())
        .nickname(nickname)
        .communityCategory(communityBoard.getCommunityCategory())
        .title(communityBoard.getTitle())
        .content(communityBoard.getContent())
        .createdAt(communityBoard.getCreatedDate())
        .lastModifiedAt(communityBoard.getLastModifiedDate())
        .build();
  }

}