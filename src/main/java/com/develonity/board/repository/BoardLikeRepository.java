package com.develonity.board.repository;

import com.develonity.board.entity.BoardLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BoardLikeRepository extends JpaRepository<BoardLike, Long> {

  boolean existsBoardLikeByBoardIdAndUserId(Long boardId, Long userId);

  boolean existsBoardLikeById(Long boardId);

  BoardLike findByBoardIdAndUserId(Long boardId, Long userId);

  int countByBoardId(Long boardId);

  @Modifying
  @Query("delete from BoardLike l where l.boardId in :boardId")
  void deleteAllByBoardId(@Param("boardId") Long boardId);
}
