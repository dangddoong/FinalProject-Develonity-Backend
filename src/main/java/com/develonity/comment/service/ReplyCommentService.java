package com.develonity.comment.service;

import com.develonity.comment.dto.ReplyCommentRequest;
import com.develonity.comment.entity.Comment;
import com.develonity.comment.entity.ReplyComment;
import com.develonity.user.entity.User;

public interface ReplyCommentService {

  void createReplyComment(Long commentId, ReplyCommentRequest request, User user);

  void updateReplyComment(Long commentId, Long replyCommentId, ReplyCommentRequest request,
      User user);

  void deleteReplyComment(Long replyCommentId, User user);

  String getNickname(Long userId);

  String getNicknameByReplyComment(ReplyComment replyComment);

  void deleteAllReplyComments(Comment comment);
}
