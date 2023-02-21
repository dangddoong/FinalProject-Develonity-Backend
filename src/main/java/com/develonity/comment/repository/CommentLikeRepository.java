package com.develonity.comment.repository;

import com.develonity.comment.entity.CommentLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {

  int countByCommentId(Long commentId);

  boolean existsCommentLikeByCommentIdAndUserId(Long commentId, Long userId);

  CommentLike findByCommentIdAndUserId(Long commentId, Long UserId);

  boolean existsCommentById(Long commentId);

  @Modifying(clearAutomatically = true, flushAutomatically = true)
  @Query("delete from CommentLike l where l.commentId in :commentId")
  void deleteAllByCommentId(@Param("commentId") Long commentId);

}
