package com.develonity.board.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.develonity.board.entity.CommunityBoard;
import com.develonity.board.repository.BoardLikeRepository;
import com.develonity.board.repository.CommunityBoardRepository;
import com.develonity.user.entity.User;
import com.develonity.user.repository.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BoardLikeServiceImplTest {

  @Autowired
  private UserRepository userRepository;
  @Autowired
  private CommunityBoardRepository communityBoardRepository;

  @Autowired
  private BoardLikeService boardLikeService;

  @Autowired
  private BoardLikeRepository boardLikeRepository;

  @Test
  @DisplayName("좋아요 추가 & 삭제 & 예외")
  void addBoardLikeAndCancel() {
    Optional<User> findUser = userRepository.findById(1L);
    Optional<CommunityBoard> findCommunityBoard = communityBoardRepository.findById(1L);

    boardLikeService.addBoardLike(findUser.get().getId(), findCommunityBoard.get().getId());

    assertThat(boardLikeRepository.findByBoardIdAndUserId(findCommunityBoard.get().getId(),
        findUser.get().getId())).isNotNull();
    //이미 좋아요 누른 경우 예외 떠야함

    assertThrows(Exception.class,
        () -> boardLikeService.addBoardLike(findUser.get().getId(),
            findCommunityBoard.get().getId()));
    boardLikeService.cancelBoardLike
        (findCommunityBoard.get().getId(),
            findUser.get().getId());

    assertThat(boardLikeRepository.findByBoardIdAndUserId(findCommunityBoard.get().getId(),
        findUser.get().getId())).isNull();

    //이미 좋아요 취소한 경우 예외 떠야함
    assertThrows(Exception.class,
        () -> boardLikeService.cancelBoardLike(findCommunityBoard.get().getId(),
            findUser.get().getId()));


  }
}