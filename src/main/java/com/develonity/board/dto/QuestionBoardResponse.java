package com.develonity.board.dto;

import com.develonity.board.entity.BoardStatus;
import com.develonity.board.entity.Category;
import com.develonity.board.entity.QuestionBoard;
import com.develonity.board.entity.SubCategory;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class QuestionBoardResponse {

  private final Long id;
  private final String nickname;
  private final Category category;
  private final SubCategory subCategory;
  private final String title;
  private final String content;
  private final int boardLike;
  private final int prizePoint;
  private final BoardStatus status;
  private final LocalDateTime createdAt;
  private final LocalDateTime lastModifiedAt;

  private final List<String> imagePaths;
  private final Boolean isLike; //로그인한 유저가 좋아요 눌렀는지 여부


  public QuestionBoardResponse(QuestionBoard questionBoard, String nickname, int boardLike,
      Boolean isLike, List<String> imagePaths) {
    this.id = questionBoard.getId();
    this.nickname = nickname;
    this.category = questionBoard.getCategory();
    this.subCategory = questionBoard.getSubCategory();
    this.title = questionBoard.getTitle();
    this.content = questionBoard.getContent();
    this.boardLike = boardLike;
    this.prizePoint = questionBoard.getPrizePoint();
    this.status = questionBoard.getStatus();
    this.createdAt = questionBoard.getCreatedDate();
    this.lastModifiedAt = questionBoard.getLastModifiedDate();
    this.isLike = isLike;
    this.imagePaths = imagePaths;
  }

  public static QuestionBoardResponse toQuestionBoardResponse(QuestionBoard questionBoard,
      String nickname) {
    return QuestionBoardResponse.builder()
        .id(questionBoard.getId())
        .nickname(nickname)
        .category(questionBoard.getCategory())
        .subCategory(questionBoard.getSubCategory())
        .title(questionBoard.getTitle())
        .content(questionBoard.getContent())
        .prizePoint(questionBoard.getPrizePoint())
        .status(questionBoard.getStatus())
        .createdAt(questionBoard.getCreatedDate())
        .lastModifiedAt(questionBoard.getLastModifiedDate())
        .build();
  }
}
