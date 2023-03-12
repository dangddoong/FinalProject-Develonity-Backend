package com.develonity.board.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.develonity.board.dto.CommunityBoardRequest;
import com.develonity.board.entity.CommunityBoard;
import com.develonity.board.entity.CommunityCategory;
import com.develonity.board.repository.CommunityBoardRepository;
import com.develonity.board.service.CommunityBoardService;
import com.develonity.user.entity.User;
import com.develonity.user.entity.UserRole;
import com.develonity.user.repository.UserRepository;
import com.develonity.user.service.UserService;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@SpringBootTest
class BoardControllerTest {

  @Autowired
  private UserRepository userRepository;
  @Autowired
  private CommunityBoardRepository communityBoardRepository;
  @Autowired
  private CommunityBoardService communityBoardService;

  @Autowired
  private UserService userService;

  @BeforeEach
  void allDeleteBefore() {
    communityBoardRepository.deleteAll();
  }

  @AfterEach
  void allDeleteAfter() {
    communityBoardRepository.deleteAll();
  }

  @Test
  @Transactional
  @DisplayName("등업 수락")
  void changeGrade() throws IOException {

    Optional<User> findUser = userRepository.findById(1L);

    CommunityBoardRequest request = new CommunityBoardRequest("제목생성", "내용생성",
        CommunityCategory.GRADE);
    List<MultipartFile> multipartFiles = new ArrayList<>();
    CommunityBoard createCommunityBoard = communityBoardService.createCommunityBoard(request,
        multipartFiles, findUser.get());

    assertThat(findUser.get().getUserRole().equals(UserRole.AMATEUR)).isTrue();

    //respectPoint 부족하니까 예외 떠야함
    assertThrows(Exception.class,
        () -> communityBoardService.upgradeGrade(createCommunityBoard.getUserId(),
            createCommunityBoard.getId()));
//포인트 추가
    findUser.get().addRespectPoint(20);
    userRepository.save(findUser.get());
//등업
    communityBoardService.upgradeGrade(createCommunityBoard.getUserId(),
        createCommunityBoard.getId());

    assertThat(findUser.get().getUserRole().equals(UserRole.EXPERT)).isTrue();
  }
}