package com.develonity.board.service;

import com.develonity.board.entity.BoardLike;
import com.develonity.board.repository.BoardLikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BoardLikeServiceImpl implements BoardLikeService {

  private final BoardLikeRepository boardLikeRepository;

  @Transactional
  @Override
  public void changeBoardLike(Long userId, Long boardId) {

    if (boardLikeRepository.existsBoardLikeByBoardIdAndUserId(boardId, userId)) {
      BoardLike boardLike = boardLikeRepository.findByBoardIdAndUserId(boardId, userId);
      boardLikeRepository.delete(boardLike);
    } else {
      BoardLike boardLike = new BoardLike(userId, boardId);
      boardLikeRepository.save(boardLike);
    }
  }

//
//  public void addBoardLike(Long userId, Long boardId){
//    if(!boardLikeRepository.existsBoardLikeByBoardIdAndUserId(boardId, userId)) {
//      BoardLike boardLike = new BoardLike(userId, boardId);
//      boardLikeRepository.save(boardLike);
//    }
//  }
//
//  public void cancelBoardLike(Long userId, Long boardId) {
//    if (boardLikeRepository.existsBoardLikeByBoardIdAndUserId(boardId, userId)) {
//      BoardLike boardLike = boardLikeRepository.findByBoardIdAndUserId(boardId, userId);
//      boardLikeRepository.delete(boardLike);
//    }
//  }

  public int countLike(Long boardId) {
    return boardLikeRepository.countByBoardId(boardId);
  }

  @Override
  public void deleteLike(Long boardId) {
    boardLikeRepository.deleteById(boardId);

  }

  @Override
  public boolean isExistLikes(Long boardId) {
    return boardLikeRepository.existsBoardById(boardId);
  }


}



