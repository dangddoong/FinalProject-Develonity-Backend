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
  private final LocalDateTime createdAt;
  private final LocalDateTime lastModifiedAt;
  private final long countAllComments;
  private final long boardLike;

  private boolean hasLike;

  private List<String> imagePaths;

  //querydsl용..
  public CommunityBoardResponse(
      Long id,
      String nickname,
      CommunityCategory communityCategory,
      String title,
      String content,
      LocalDateTime createdAt,
      LocalDateTime lastModifiedAt,
      long countAllComment,
      long boardLike
  ) {
    this.id = id;
    this.nickname = nickname;
    this.communityCategory = communityCategory;
    this.title = title;
    this.content = content;
    this.boardLike = boardLike;
    this.createdAt = createdAt;
    this.lastModifiedAt = lastModifiedAt;
    this.countAllComments = countAllComment;
  }


  public CommunityBoardResponse(CommunityBoard communityBoard, String nickname, long boardLike,
      Boolean isLike, List<String> imagePaths, long countAllComments) {
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
    this.countAllComments = countAllComments;


  }

  public static CommunityBoardResponse toCommunityBoardResponse(CommunityBoard communityBoard,
      String nickname, long countAllComments) {
    return CommunityBoardResponse.builder()
        .id(communityBoard.getId())
        .nickname(nickname)
        .communityCategory(communityBoard.getCommunityCategory())
        .title(communityBoard.getTitle())
        .content(communityBoard.getContent())
        .createdAt(communityBoard.getCreatedDate())
        .lastModifiedAt(communityBoard.getLastModifiedDate())
        .countAllComments(countAllComments)
        .build();
  }
}
