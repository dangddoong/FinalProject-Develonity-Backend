package com.develonity.comment.repository;

import com.develonity.comment.entity.CommentLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {

  int countByCommentId(Long commentId);

  boolean existsCommentLikeByCommentIdAndUserId(Long commentId, Long userId);

  CommentLike findByCommentIdAndUserId(Long commentId, Long UserId);

  boolean existsCommentById(Long commentId);

  void deleteAllByCommentId(Long commentId);
}
