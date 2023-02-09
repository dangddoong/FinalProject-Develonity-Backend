package com.develonity.comment.service;

import com.develonity.comment.dto.CommentRequest;
import com.develonity.comment.dto.CommentResponse;
import com.develonity.user.entity.User;
import java.util.List;
import org.springframework.data.domain.Page;

public interface CommentService {

  Page<CommentResponse> getAllComment(int page, int size);

  List<CommentResponse> getMyComments(int page, int size, Long userId,
      User user);

  CommentResponse createQuestionComment(Long boardId, CommentRequest requestDto, User user);

  CommentResponse updateQuestionComment(Long boardId, Long commentId, CommentRequest request,
      User user);
}
