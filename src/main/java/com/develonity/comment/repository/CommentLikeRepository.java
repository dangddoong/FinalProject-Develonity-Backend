package com.develonity.comment.repository;

import com.develonity.comment.entity.CommentLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {

  int countByCommentId(Long commentId);
}
