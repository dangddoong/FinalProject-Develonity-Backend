package com.develonity.board.repository;

import com.develonity.board.entity.Board;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {

  Page<Board> findBoardsByUserId(Long userId, Pageable pageable);

  List<Board> findAllByUserId(Long userId);


  List<Board> findAllByIdIn(List<Long> boardIds);

}
