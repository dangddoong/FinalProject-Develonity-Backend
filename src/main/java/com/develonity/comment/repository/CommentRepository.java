package com.develonity.comment.repository;

import com.develonity.comment.entity.Comment;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommentRepository extends JpaRepository<Comment, Long> {

  Page<Comment> findAllByUserId(Pageable pageable, Long userId);

  Page<Comment> findAllByBoardId(Pageable pageable, Long boardId);


  @Modifying(clearAutomatically = true, flushAutomatically = true)
  @Query("delete from Comment c where c.boardId in :boardId")
  void deleteAllByBoardId(@Param("boardId") Long boardId);

  Page<Comment> findBy(Pageable pageable);

  List<Comment> findAllByBoardId(Long boardId);

  boolean existsCommentByBoardIdAndUserId(Long boardId, Long userId);

  boolean existsCommentsByBoardId(Long boardId);

}
