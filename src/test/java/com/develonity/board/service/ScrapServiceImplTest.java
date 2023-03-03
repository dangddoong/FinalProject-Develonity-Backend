package com.develonity.board.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.develonity.board.entity.CommunityBoard;
import com.develonity.board.entity.Scrap;
import com.develonity.board.repository.CommunityBoardRepository;
import com.develonity.board.repository.ScrapRepository;
import com.develonity.user.entity.User;
import com.develonity.user.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ScrapServiceImplTest {

  @Autowired
  private UserRepository userRepository;
  @Autowired
  private CommunityBoardRepository communityBoardRepository;

  @Autowired
  private ScrapRepository scrapRepository;

  @Autowired
  private ScrapService scrapService;

  @Test
  @DisplayName("스크랩 추가 & 삭제 & 예외")
  void addScrap() {
    Optional<User> findUser = userRepository.findById(1L);
    Optional<CommunityBoard> findCommunityBoard = communityBoardRepository.findById(1L);

    scrapService.addScrap(findUser.get().getId(), findCommunityBoard.get().getId());

    assertThat(scrapRepository.findByBoardIdAndUserId(findCommunityBoard.get().getId(),
        findUser.get().getId())).isNotNull();
    //이미 스크랩 추가한 경우 예외 떠야함
    assertThrows(Exception.class,
        () -> scrapService.addScrap(findUser.get().getId(), findCommunityBoard.get().getId()));

    scrapService.cancelScrap(
        findCommunityBoard.get().getId(),
        findUser.get().getId());

    assertThat(scrapRepository.findByBoardIdAndUserId(findCommunityBoard.get().getId(),
        findUser.get().getId())).isNull();

    //이미 좋아요 취소한 경우 예외 떠야함
    assertThrows(Exception.class, () -> scrapService.cancelScrap(
        findCommunityBoard.get().getId(),
        findUser.get().getId()));


  }

  @Test
  @DisplayName("스크랩한 게시물 조회")
  void getScrapBoardIds() {

    Optional<User> findUser = userRepository.findById(1L);
    Optional<CommunityBoard> findCommunityBoard = communityBoardRepository.findById(1L);
    Optional<CommunityBoard> findCommunityBoard2 = communityBoardRepository.findById(2L);

    scrapService.addScrap(findUser.get().getId(), findCommunityBoard.get().getId());
    scrapService.addScrap(findUser.get().getId(), findCommunityBoard2.get().getId());

    List<Scrap> scraps = scrapRepository.findAllByUserId(findUser.get().getId());

    assertThat(scraps).size().isEqualTo(2);
  }
}



