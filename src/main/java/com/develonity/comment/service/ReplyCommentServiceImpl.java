package com.develonity.comment.service;

import com.develonity.comment.dto.ReplyCommentRequest;
import com.develonity.comment.dto.ReplyCommentResponse;
import com.develonity.comment.entity.Comment;
import com.develonity.comment.entity.ReplyComment;
import com.develonity.comment.repository.CommentRepository;
import com.develonity.comment.repository.ReplyCommentRepository;
import com.develonity.common.exception.CustomException;
import com.develonity.common.exception.ExceptionStatus;
import com.develonity.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReplyCommentServiceImpl implements ReplyCommentService {

  private final CommentRepository commentRepository;
  private final ReplyCommentRepository replyCommentRepository;

  // 댓글 확인
  private Comment getComment(Long commentId) {
    return commentRepository.findById(commentId).orElseThrow(
        () -> new CustomException(ExceptionStatus.COMMENT_IS_NOT_EXIST)
    );
  }

  // 대댓글 확인
  private ReplyComment getReplyComment(Long replyCommentId) {
    return replyCommentRepository.findById(replyCommentId).orElseThrow(
        () -> new CustomException(ExceptionStatus.REPLY_COMMENT_IS_NOT_EXIST)
    );
  }

  // 댓글 작성자 확인
  private void checkUser(User user, ReplyComment replyComment) {
    if (user.getNickName() != replyComment.getNickName()) {
      throw new CustomException(ExceptionStatus.REPLY_COMMENT_USER_NOT_MATCH);
    }
  }

  // 대댓글 작성
  @Override
  @Transactional
  public void createReplyComment(Long commentId, ReplyCommentRequest request, User user) {
    // 댓글이 있는지 확인
    Comment comment = getComment(commentId);

    // 대댓글 작성
    ReplyComment replyComment = new ReplyComment(user, request, comment);
    replyCommentRepository.save(replyComment);
    new ReplyCommentResponse(replyComment);
  }

  // 대댓글 수정
  @Override
  @Transactional
  public void updateReplyComment(Long commentId, Long replyCommentId, ReplyCommentRequest request,
      User user) {
    // 댓글이 있는지 확인
    Comment comment = getComment(commentId);
    // 대댓글이 있는지 확인
    ReplyComment replyComment = getReplyComment(replyCommentId);
    // 작성자가 맞는지 확인
    checkUser(user, replyComment);
    // 댓글 수정
    replyComment.updateReplyComment(request.getContent());
    replyCommentRepository.save(replyComment);
    new ReplyCommentResponse(replyComment);
  }

  // 대댓글 삭제
  @Override
  @Transactional
  public void deleteReplyComment(Long replyCommentId, User user) {
    // 대댓글이 있는지 확인
    ReplyComment replyComment = getReplyComment(replyCommentId);
    // 작성자가 맞는지 확인
    checkUser(user, replyComment);
    // 댓글 삭제
    replyCommentRepository.delete(replyComment);
  }
}
