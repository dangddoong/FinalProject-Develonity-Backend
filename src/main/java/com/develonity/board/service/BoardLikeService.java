package com.develonity.board.service;

public interface BoardLikeService {

  void changeBoardLike(Long userId, Long boardId);

  int countLike(Long boardId);

  void deleteLike(Long boardId);

  boolean isExistLikes(Long boardId);

//  void addBoardLike(Long userId, Long boardId);

//  void cancelBoardLike(Long userId, Long boardId);

}
