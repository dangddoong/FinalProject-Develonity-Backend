package com.develonity.comment.service;

import com.develonity.comment.entity.CommentLike;
import com.develonity.comment.repository.CommentLikeRepository;
import com.develonity.common.exception.CustomException;
import com.develonity.common.exception.ExceptionStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentLikeServiceImpl implements CommentLikeService {

  private final CommentLikeRepository commentLikeRepository;

  // 좋아요를 추가하는 기능
  @Override
  @Transactional
  public void addCommentLike(Long commentId, Long userId) {
    if (isExistLikesCommentIdAndUserId(commentId, userId)) {
      throw new CustomException(ExceptionStatus.LIKE_IS_EXIST);
    }
    CommentLike commentLike = new CommentLike(userId, commentId);
    commentLikeRepository.save(commentLike);
  }

  // 좋아요 취소 기능
  @Override
  @Transactional
  public void cancelCommentLike(Long commentId, Long userId) {
    if (!isExistLikesCommentIdAndUserId(commentId, userId)) {
      throw new CustomException(ExceptionStatus.LIKE_IS_NOT_EXIST);
    }
    CommentLike commentLike = commentLikeRepository.findByCommentIdAndUserId(commentId, userId);
    commentLikeRepository.delete(commentLike);
  }

  // 좋아요 카운트 (좋아요 1 올리는?) 기능
  @Override
  public int countLike(Long commentId) {
    return commentLikeRepository.countByCommentId(commentId);
  }

  // 좋아요 카운트 (좋아요 1 내리는?) 기능
  @Override
  public void deleteLike(Long commentId) {
    commentLikeRepository.deleteById(commentId);
  }

  // 좋아요가 이미 있는지 확인
  @Override
  public boolean isExistLikes(Long commentId) {
    return commentLikeRepository.existsCommentById(commentId);
  }

  // 유저가 이미 댓글에 좋아요를 눌렀는지 확인
  @Override
  public boolean isExistLikesCommentIdAndUserId(Long commentId, Long userId) {
    return commentLikeRepository.existsCommentLikeByCommentIdAndUserId(commentId, userId);
  }

  // 좋아요를 눌렀는지 안눌렀는지 확인하는 기능
  @Override
  public boolean isLike(Long commentId, Long userId) {
    return commentLikeRepository.existsCommentLikeByCommentIdAndUserId(commentId, userId);
  }

  @Override
  public void deleteAllByCommentId(Long commentId){
    commentLikeRepository.deleteAllByCommentId(commentId);
  }

}
