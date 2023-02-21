package com.develonity.comment.repository;

import com.develonity.comment.entity.Comment;
import com.develonity.comment.entity.ReplyComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReplyCommentRepository extends JpaRepository<ReplyComment, Long> {

  @Modifying(clearAutomatically = true, flushAutomatically = true)
  @Query("delete from ReplyComment r where r.comment in :comment")
  void deleteAllByCommentId(@Param("comment") Comment comment);
}
