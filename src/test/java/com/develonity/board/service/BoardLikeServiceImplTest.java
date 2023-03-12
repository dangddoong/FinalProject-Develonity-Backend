package com.develonity.board.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.develonity.board.dto.CommunityBoardRequest;
import com.develonity.board.entity.CommunityBoard;
import com.develonity.board.entity.CommunityCategory;
import com.develonity.board.repository.BoardLikeRepository;
import com.develonity.board.repository.CommunityBoardRepository;
import com.develonity.user.entity.User;
import com.develonity.user.repository.UserRepository;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.multipart.MultipartFile;

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

  @Autowired
  private CommunityBoardService communityBoardService;

  @BeforeEach
  void allDeleteBefore() {
    communityBoardRepository.deleteAll();
  }

  @AfterEach
  void allDeleteAfter() {
    communityBoardRepository.deleteAll();
    boardLikeRepository.deleteAll();
  }

  @Test
  @DisplayName("좋아요 추가 & 삭제 & 예외")
  void addBoardLikeAndCancel() throws IOException {
    Optional<User> findUser = userRepository.findById(1L);
    CommunityBoardRequest request = new CommunityBoardRequest("제목2", "내용2",
        CommunityCategory.NORMAL);
    List<MultipartFile> multipartFiles = new ArrayList<>();
    CommunityBoard createCommunityBoard = communityBoardService.createCommunityBoard(request,
        multipartFiles, findUser.get());

    boardLikeService.addBoardLike(findUser.get().getId(), createCommunityBoard.getId());

    assertThat(boardLikeRepository.findByBoardIdAndUserId(createCommunityBoard.getId(),
        findUser.get().getId())).isNotNull();
    //이미 좋아요 누른 경우 예외 떠야함

    assertThrows(Exception.class,
        () -> boardLikeService.addBoardLike(findUser.get().getId(),
            createCommunityBoard.getId()));
    boardLikeService.cancelBoardLike
        (findUser.get().getId(), createCommunityBoard.getId()
        );

    assertThat(boardLikeRepository.findByBoardIdAndUserId(createCommunityBoard.getId(),
        findUser.get().getId())).isNull();

    //이미 좋아요 취소한 경우 예외 떠야함
    assertThrows(Exception.class,
        () -> boardLikeService.cancelBoardLike(createCommunityBoard.getId(),
            findUser.get().getId()));

  }
}