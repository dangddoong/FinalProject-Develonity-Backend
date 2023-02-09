package com.develonity.board.dto;

import com.develonity.board.entity.BoardStatus;
import com.develonity.board.entity.Category;
import com.develonity.board.entity.QuestionBoard;
import com.develonity.user.entity.User;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class QuestionBoardResponse {

  private final Long id;
  private final String nickname;
  private final Category category;
  private final String title;
  private final String content;
  //  private final int postLike;
  private final int prizePoint;
  private final BoardStatus status;
  private final String imageUrl;
  private final LocalDateTime createdAt;
//  private final LocalDateTime modifiedAt;

  public QuestionBoardResponse(QuestionBoard questionBoard, User user) {
    this.id = questionBoard.getId();
    this.nickname = user.getNickName();
    this.category = questionBoard.getCategory();
    this.title = questionBoard.getTitle();
    this.content = questionBoard.getContent();
//    this.postLike = postlike.get;
    this.prizePoint = questionBoard.getPrizePoint();
    this.status = questionBoard.getStatus();
    this.imageUrl = questionBoard.getImageUrl();
    this.createdAt = questionBoard.getCreatedDate();
//    this.createdAt = questionBoard.get
//    this.modifiedAt = questionBoard.get
  }
//  private final List<CommentResponseDto> comments = new ArrayList<>();

}