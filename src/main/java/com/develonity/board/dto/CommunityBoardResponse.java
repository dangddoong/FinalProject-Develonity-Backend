package com.develonity.board.dto;

import com.develonity.board.entity.Category;
import com.develonity.board.entity.CommunityBoard;
import com.develonity.user.entity.User;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CommunityBoardResponse {

  private final Long id;
  private final String nickname;
  private final Category category;
  private final String title;
  private final String content;
  private final int boardLike;
  private final LocalDateTime createdAt;
  private final LocalDateTime lastModifiedAt;

  private Boolean isLike; //로그인한 유저가 좋아요 눌렀는지 여부


  public CommunityBoardResponse(CommunityBoard communityBoard, User user, int boardLike,
      Boolean isLike) {
    this.id = communityBoard.getId();
    this.nickname = user.getNickName();
    this.category = communityBoard.getCategory();
    this.title = communityBoard.getTitle();
    this.content = communityBoard.getContent();
    this.boardLike = boardLike;
    this.createdAt = communityBoard.getCreatedDate();
    this.lastModifiedAt = communityBoard.getLastModifiedDate();
    this.isLike = isLike;


  }

  public static CommunityBoardResponse toCommunityBoardResponse(CommunityBoard communityBoard,
      User user) {
    return CommunityBoardResponse.builder()
        .id(communityBoard.getId())
        .nickname(user.getNickName())
        .category(communityBoard.getCategory())
        .title(communityBoard.getTitle())
        .content(communityBoard.getContent())
        .createdAt(communityBoard.getCreatedDate())
        .lastModifiedAt(communityBoard.getLastModifiedDate())
        .build();
  }

}
