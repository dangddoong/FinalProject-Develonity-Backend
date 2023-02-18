package com.develonity.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ExceptionStatus {
  // 대문자로 작성해주세요.

  NOT_ALLOWED(400, "본인 질문글에 대한 답변 작성은 불가능합니다."),

  AUTHENTICATED_EXCEPTION(401, " 인증 실패에 따른 예외가 발생했습니다. "),
  PASSWORDS_DO_NOT_MATCH(401, "비밀번호가 일치 하지 않습니다."),
  COMMENT_USER_NOT_MATCH(401, "댓글 작성자가 아닙니다."),
  BOARD_USER_NOT_MATCH(401, "게시글 작성자가 아닙니다."),
  REPLY_COMMENT_USER_NOT_MATCH(401, "대댓글 작성자가 아닙니다."),
  CATEGORY_DO_NOT_MATCH(401, "카테고리가 일치하지 않습니다."),
  IS_NOT_CORRECT_FORMAT(401, "올바른 형식의 파일이 아닙니다"),
  USER_IS_NOT_EXIST(404, "사용자가 존재 하지 않습니다."),
  ORDER_IS_NOT_EXIST(404, " 주문내역이 존재하지 않습니다. "),
  CATEGORY_IS_NOT_EXIST(404, "카테고리가 존재하지 않습니다."),
  PAGINATION_IS_NOT_EXIST(404, "요청하신 페이지 내역이 존재하지 않습니다."),
  GIFTCARD_IS_NOT_EXIST(404, "기프트카드가 존재하지 않습니다."),
  COMMENT_IS_NOT_EXIST(404, "댓글이 존재하지 않습니다."),
  BOARD_IS_NOT_EXIST(404, "게시글이 존재하지 않습니다."),
  LIKE_IS_NOT_EXIST(404, "좋아요가 존재하지 않습니다."),
  REPLY_COMMENT_IS_NOT_EXIST(404, "대댓글이 존재하지 않습니다."),

  USERID_IS_EXIST(409, " 이미 등록된 아이디입니다. "),
  POINTS_IS_LACKING(409, "포인트가 부족합니다."),
  QUANTITY_IS_LACKING(409, "재고가 부족합니다."),
  GIFTCARD_IS_EXIST(409, "이미 등록된 기프트카드 입니다."),
  LIKE_IS_EXIST(409, "이미 좋아요를 누른 게시글입니다."),
  ALREADY_ADOPTED(409, "이미 답변 채택된 게시글입니다.");

  private final int statusCode;
  private final String message;

}


