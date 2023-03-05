package com.develonity.comment.service;

import com.develonity.comment.dto.ReplyCommentRequest;
import com.develonity.comment.dto.ReplyCommentResponse;
import com.develonity.comment.entity.Comment;
import com.develonity.comment.entity.ReplyComment;
import com.develonity.user.entity.User;
import java.util.List;

public interface ReplyCommentService {

  ReplyComment createReplyComment(Long commentId, ReplyCommentRequest request, User user);

  ReplyComment updateReplyComment(Long commentId, Long replyCommentId, ReplyCommentRequest request,
      User user);

  void deleteReplyComment(Long replyCommentId, User user);

  String getNickname(Long userId);

  String getNicknameByReplyComment(ReplyComment replyComment);

  void deleteAllReplyComments(Comment comment);

  long countReplyComments(List<Comment> comments);

  List<ReplyCommentResponse> getReplyCommentResponses(Comment comment);
}
