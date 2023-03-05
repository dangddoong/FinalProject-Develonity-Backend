package com.develonity.comment.service;

public interface CommentLikeService {

  void addCommentLike(Long commentId, Long userId);

  void cancelCommentLike(Long commentId, Long userId);

  long countLike(Long commentId);

  void deleteLike(Long commentId);

  boolean isExistLikes(Long commentId);

  boolean isExistLikesCommentIdAndUserId(Long commentId, Long userId);

  void deleteAllByCommentId(Long commentId);
}
