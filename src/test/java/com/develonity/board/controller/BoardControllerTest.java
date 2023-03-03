package com.develonity.board.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.develonity.board.entity.CommunityBoard;
import com.develonity.board.repository.CommunityBoardRepository;
import com.develonity.board.service.CommunityBoardService;
import com.develonity.user.entity.User;
import com.develonity.user.entity.UserRole;
import com.develonity.user.repository.UserRepository;
import com.develonity.user.service.UserService;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

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

  @Test
  @Transactional
  @DisplayName("등업 수락")
  void changeGrade() {

    Optional<User> findUser = userRepository.findById(1L);
    Optional<CommunityBoard> findCommunityBoard = communityBoardRepository.findById(1L);

    assertThat(findUser.get().getUserRole().equals(UserRole.AMATEUR)).isTrue();

    //respectPoint 부족하니까 예외 떠야함
    assertThrows(Exception.class,
        () -> communityBoardService.upgradeGrade(findCommunityBoard.get().getUserId(),
            findCommunityBoard.get().getId()));
//포인트 추가
    findUser.get().addRespectPoint(20);
    userRepository.save(findUser.get());
//등업
    communityBoardService.upgradeGrade(findCommunityBoard.get().getUserId(),
        findCommunityBoard.get().getId());

    assertThat(findUser.get().getUserRole().equals(UserRole.EXPERT)).isTrue();
  }
}