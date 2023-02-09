package com.develonity.board.repository;

import com.develonity.board.entity.BoardLike;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardLikeRepository extends JpaRepository<BoardLike, Long> {

  boolean existsBoardLikeByBoardIdAndUserId(Long boardId, Long userId);

  BoardLike findByBoardIdAndUserId(Long boardId, Long userId);

  List<BoardLike> findAllByBoardId(Long boardId);

}
