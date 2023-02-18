package com.develonity.board.service;

public interface BoardLikeService {

  int countLike(Long boardId);

  void deleteLikes(Long boardId);

  boolean isExistLikes(Long boardId);

  boolean isLike(Long boardId, Long userId);

  void addBoardLike(Long userId, Long boardId);

  void cancelBoardLike(Long userId, Long boardId);

  boolean isExistLikesBoardIdAndUserId(Long boardId, Long userId);


}
