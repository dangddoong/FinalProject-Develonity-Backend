package com.develonity.board.service;

public interface BoardLikeService {

  void changeBoardLike(Long userId, Long boardId);

  int countLike(Long boardId);

}
