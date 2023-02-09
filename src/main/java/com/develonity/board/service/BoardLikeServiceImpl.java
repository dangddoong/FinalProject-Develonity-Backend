package com.develonity.board.service;

import com.develonity.board.entity.BoardLike;
import com.develonity.board.repository.BoardLikeRepository;
import java.util.List;
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

  public int countLike(Long boardId) {
    List<BoardLike> boardLikes = boardLikeRepository.findAllByBoardId(boardId);
    return boardLikes.size();
  }
}



