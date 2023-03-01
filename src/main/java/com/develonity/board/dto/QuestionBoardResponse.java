package com.develonity.board.dto;

import com.develonity.board.entity.BoardStatus;
import com.develonity.board.entity.QuestionBoard;
import com.develonity.board.entity.QuestionCategory;
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

  private final QuestionCategory questionCategory;
  private final String title;
  private final String content;
  private final long boardLike;
  private final int prizePoint;
  private final BoardStatus status;
  private final LocalDateTime createdAt;
  private final LocalDateTime lastModifiedAt;
  private List<String> imagePaths;
  private Boolean hasLike; //로그인한 유저가 좋아요 눌렀는지 여부

  private final long countComments;

  //queryDsl용..
  public QuestionBoardResponse(
      Long id,
      String nickname,
      QuestionCategory questionCategory,
      String title,
      String content,
      int prizePoint,
      BoardStatus status,
      LocalDateTime createdAt,
      LocalDateTime lastModifiedAt,
      long countComments,
      long boardLike
  ) {
    this.id = id;
    this.nickname = nickname;
    this.questionCategory = questionCategory;
    this.title = title;
    this.content = content;
    this.boardLike = boardLike;
    this.prizePoint = prizePoint;
    this.status = status;
    this.createdAt = createdAt;
    this.lastModifiedAt = lastModifiedAt;
    this.countComments = countComments;
  }

  public QuestionBoardResponse(QuestionBoard questionBoard, String nickname, long boardLike,
      Boolean hasLike, List<String> imagePaths, long countComments) {
    this.id = questionBoard.getId();
    this.nickname = nickname;
    this.questionCategory = questionBoard.getQuestionCategory();
    this.title = questionBoard.getTitle();
    this.content = questionBoard.getContent();
    this.boardLike = boardLike;
    this.prizePoint = questionBoard.getPrizePoint();
    this.status = questionBoard.getStatus();
    this.createdAt = questionBoard.getCreatedDate();
    this.lastModifiedAt = questionBoard.getLastModifiedDate();
    this.hasLike = hasLike;
    this.imagePaths = imagePaths;
    this.countComments = countComments;
  }

  public static QuestionBoardResponse toQuestionBoardResponse(QuestionBoard questionBoard,
      String nickname, long countComments) {
    return QuestionBoardResponse.builder()
        .id(questionBoard.getId())
        .nickname(nickname)
        .questionCategory(questionBoard.getQuestionCategory())
        .title(questionBoard.getTitle())
        .content(questionBoard.getContent())
        .prizePoint(questionBoard.getPrizePoint())
        .status(questionBoard.getStatus())
        .createdAt(questionBoard.getCreatedDate())
        .lastModifiedAt(questionBoard.getLastModifiedDate())
        .countComments(countComments)
        .build();
  }
}

