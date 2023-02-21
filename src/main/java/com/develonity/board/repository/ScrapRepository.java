package com.develonity.board.repository;

import com.develonity.board.entity.Scrap;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ScrapRepository extends JpaRepository<Scrap, Long> {

  boolean existsScrapByBoardIdAndUserId(Long boardId, Long userId);

  boolean existsScrapById(Long boardId);

  Scrap findByBoardIdAndUserId(Long boardId, Long userId);

  int countByUserId(Long userId);

  @Modifying(clearAutomatically = true, flushAutomatically = true)
  @Query("delete from Scrap s where s.boardId in :boardId")
  void deleteAllByBoardId(@Param("boardId") Long boardId);

  //테스트용
  List<Scrap> findAllByUserId(Long userId);

}
