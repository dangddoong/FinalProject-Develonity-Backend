package com.develonity.board.repository;

import com.develonity.board.entity.QuestionBoard;
import com.develonity.board.entity.QuestionCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionBoardRepository extends JpaRepository<QuestionBoard, Long> {

  boolean existsBoardById(Long id);

  QuestionBoard findBoardById(Long id);

  Page<QuestionBoard> findByQuestionCategoryAndTitleContainingOrContentContaining(
      QuestionCategory questionCategory, String title, String content,
      Pageable pageable);

  Page<QuestionBoard> findByTitleContaining(String title,
      Pageable pageable);


}
