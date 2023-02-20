package com.develonity.board.service;

import java.util.List;

public interface ScrapService {

  int countScraps(Long boardId);

  void deleteScraps(Long boardId);

  void addScrap(Long userId, Long boardId);

  void cancelScrap(Long userId, Long boardId);

  boolean existsScrapBoardIdAndUserId(Long boardId, Long userId);

  List<Long> getScrapBoardIds(Long userId);
}
