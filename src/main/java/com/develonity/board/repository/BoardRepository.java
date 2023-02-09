package com.develonity.board.repository;

import com.develonity.board.entity.Board;
import com.develonity.board.entity.Category;
import com.develonity.board.entity.QuestionBoard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {

  boolean existsBoardById(Long id);

  QuestionBoard findBoardById(Long id);

  Page<QuestionBoard> findByCategoryAndTitleContaining(Category category, String title,
      Pageable pageable);


}
