package com.develonity.comment.service;

import com.develonity.user.entity.User;

public interface CommentLikeService {

  Boolean addLike(Long commentId, User user);

  Boolean deleteLike(Long commentId, User user);
}
