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

  void createQuestionComment(Long boardId, CommentRequest requestDto, User user);

  void updateQuestionComment(Long boardId, Long commentId, CommentRequest request,
      User user);

  void deleteQuestionComment(Long boardId, Long commentId, User user);

  void createCommunityComment(Long boardId, CommentRequest request, User user);

  void updateCommunityComment(Long boardId, Long commentId, CommentRequest request, User user);

  void deleteCommunity(Long boardId, Long commentId, User user);
}
