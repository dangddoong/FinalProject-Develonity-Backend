package com.develonity.board.dto;

import com.develonity.board.entity.Board;
import com.develonity.board.entity.CommunityBoard;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class BoardResponse {


  private final Long id;
  private final String nickname;
  private final String title;
  private final String content;
  private final long boardLike;
  private final LocalDateTime createdAt;
  private final LocalDateTime lastModifiedAt;
  private final List<String> imagePaths;
  private final Boolean hasLike; //로그인한 유저가 좋아요 눌렀는지 여부


  public BoardResponse(CommunityBoard communityBoard, String nickname, long boardLike,
      Boolean hasLike, List<String> imagePaths) {
    this.id = communityBoard.getId();
    this.nickname = nickname;
    this.title = communityBoard.getTitle();
    this.content = communityBoard.getContent();
    this.boardLike = boardLike;
    this.createdAt = communityBoard.getCreatedDate();
    this.lastModifiedAt = communityBoard.getLastModifiedDate();
    this.hasLike = hasLike;
    this.imagePaths = imagePaths;


  }

  public static com.develonity.board.dto.BoardResponse toBoardResponse(
      Board Board,
      String nickname) {
    return com.develonity.board.dto.BoardResponse.builder()
        .id(Board.getId())
        .nickname(nickname)
        .title(Board.getTitle())
        .content(Board.getContent())
        .createdAt(Board.getCreatedDate())
        .lastModifiedAt(Board.getLastModifiedDate())
        .build();
  }
}
