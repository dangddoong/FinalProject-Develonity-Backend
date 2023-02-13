package com.develonity.comment.service;

public interface CommentLikeService {

  int addLike(Long commentId);

  void cancelLike(Long commentId);
}
