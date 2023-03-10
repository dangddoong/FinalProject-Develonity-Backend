package com.develonity.comment.dto;

import com.develonity.comment.entity.CommentSort;
import com.develonity.comment.entity.CommentSortDirection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class CommentSearchCond {

  // 댓글로 검색..
  private String content;
  // 이름으로 검색..
  private String nickname;
  private CommentSort commentSort;
  private CommentSortDirection commentSortDirection;
}
