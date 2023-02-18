package com.develonity.comment.service;

import com.develonity.comment.dto.CommentList;
import com.develonity.comment.dto.CommentRequest;
import com.develonity.comment.dto.CommentResponse;
import com.develonity.comment.entity.Comment;
import com.develonity.user.entity.User;
import org.springframework.data.domain.Page;

public interface CommentService {

  Page<CommentResponse> getAllComment(User user, CommentList commentList);

  Page<CommentResponse> getMyComments(CommentList commentList, Long userId,
      User user);

  void createQuestionComment(Long questionBoardId, CommentRequest requestDto, User user);

  void updateQuestionComment(Long questionBoardId, Long commentId, CommentRequest request,
      User user);

  void deleteQuestionComment(Long commentId, User user);

  void createCommunityComment(Long communityBoardId, CommentRequest request, User user);

  void updateCommunityComment(Long communityBoardId, Long commentId, CommentRequest request,
      User user);

  void deleteCommunity(Long commentId, User user);

  Comment getComment(Long commentId);

  String getNickname(Long userId);

  String getNicknameByComment(Comment comment);

  void adoptComment(Comment comment);

  void deleteCommentsByBoardId(Long boardId);

}
