package com.develonity.board.service;

import com.develonity.board.entity.BoardLike;
import com.develonity.board.repository.BoardLikeRepository;
import com.develonity.common.exception.CustomException;
import com.develonity.common.exception.ExceptionStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BoardLikeServiceImpl implements BoardLikeService {

  private final BoardLikeRepository boardLikeRepository;

  @Transactional
  @Override
  public void addBoardLike(Long userId, Long boardId) {
    if (existsLikesBoardIdAndUserId(boardId, userId)) {
      throw new CustomException(ExceptionStatus.LIKE_IS_EXIST);
    }
    BoardLike boardLike = new BoardLike(userId, boardId);
    boardLikeRepository.save(boardLike);

  }

  @Transactional
  @Override
  public void cancelBoardLike(Long userId, Long boardId) {
    if (!existsLikesBoardIdAndUserId(boardId, userId)) {
      throw new CustomException(ExceptionStatus.LIKE_IS_NOT_EXIST);
    }
    BoardLike boardLike = boardLikeRepository.findByBoardIdAndUserId(boardId, userId);
    boardLikeRepository.delete(boardLike);
  }

  @Override
  public long countLikes(Long boardId) {
    return boardLikeRepository.countByBoardId(boardId);
  }

  @Override
  public void deleteLike(Long boardId) {
    boardLikeRepository.deleteAllByBoardId(boardId);

  }

  @Override
  public boolean existLikes(Long boardId) {
    return boardLikeRepository.existsBoardLikeById(boardId);
  }

  @Override
  public boolean existsLikesBoardIdAndUserId(Long boardId, Long userId) {
    return boardLikeRepository.existsBoardLikeByBoardIdAndUserId(boardId, userId);
  }

}


