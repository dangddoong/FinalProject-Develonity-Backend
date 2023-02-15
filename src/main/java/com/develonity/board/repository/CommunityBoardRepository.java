package com.develonity.board.repository;

import com.develonity.board.entity.CommunityBoard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommunityBoardRepository extends JpaRepository<CommunityBoard, Long> {

  boolean existsBoardById(Long id);

  CommunityBoard findBoardById(Long id);

  Page<CommunityBoard> findByTitleContainingOrContentContaining(
      String title, String content,
      Pageable pageable);

  Page<CommunityBoard> findByTitleContaining(String title,
      Pageable pageable);
}
