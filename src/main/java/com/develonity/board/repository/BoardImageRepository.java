package com.develonity.board.repository;

import com.develonity.board.entity.BoardImage;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BoardImageRepository extends JpaRepository<BoardImage, Long> {

  List<BoardImage> findAllByBoardId(Long boardId);

  @Modifying(clearAutomatically = true, flushAutomatically = true)
  @Query("delete from BoardImage b where b.boardId in :boardId")
  void deleteAllByBoardId(@Param("boardId") Long boardId);
}
