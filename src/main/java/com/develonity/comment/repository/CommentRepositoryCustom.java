package com.develonity.comment.repository;

import com.develonity.comment.dto.CommentPageDto;
import com.develonity.comment.dto.CommentResponse;
import com.develonity.comment.dto.CommentSearchCond;
import org.springframework.data.domain.Page;

public interface CommentRepositoryCustom {

  Page<CommentResponse> searchComment(CommentPageDto commentPageDto,
      CommentSearchCond commentSearchCond);

  Page<CommentResponse> searchMyComment(CommentPageDto commentPageDto,
      CommentSearchCond commentSearchCond, Long userId);

}
