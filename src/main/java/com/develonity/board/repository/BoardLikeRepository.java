package com.develonity.board.repository;

import com.develonity.board.entity.BoardLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardLikeRepository extends JpaRepository<BoardLike, Long> {

  boolean existsBoardLikeByBoardIdAndUserId(Long boardId, Long userId);

  boolean existsBoardLikeById(Long boardId);

  BoardLike findByBoardIdAndUserId(Long boardId, Long userId);

  int countByBoardId(Long boardId);

  void deleteAllByBoardId(Long boardId);
}
