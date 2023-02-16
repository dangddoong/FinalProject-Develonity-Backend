package com.develonity.board.repository;

import com.develonity.board.entity.BoardImage;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardImageRepository extends JpaRepository<BoardImage, Long> {

  List<BoardImage> findAllByBoardId(Long boardId);

  void deleteBoardImageByBoardId(Long boardId);
}
