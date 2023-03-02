package com.develonity.board.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.develonity.board.dto.CommunityBoardRequest;
import com.develonity.board.dto.CommunityBoardResponse;
import com.develonity.board.entity.BoardImage;
import com.develonity.board.entity.CommunityBoard;
import com.develonity.board.entity.CommunityCategory;
import com.develonity.board.repository.BoardImageRepository;
import com.develonity.board.repository.CommunityBoardRepository;
import com.develonity.common.jwt.JwtUtil;
import com.develonity.user.entity.User;
import com.develonity.user.repository.UserRepository;
import com.develonity.user.service.UserService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(Lifecycle.PER_CLASS)
class CommunityBoardServiceImplTest {

  @Autowired
  private CommunityBoardRepository communityBoardRepository;

  @Autowired
  private BoardLikeService boardLikeService;

  @Autowired
  private UserService userService;

  @Autowired
  private CommunityBoardServiceImpl communityBoardService;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private JwtUtil jwtUtil;

  @Autowired
  private BoardImageRepository boardImageRepository;

  @BeforeAll
  public void beforeAll() throws IOException {
    CommunityBoardRequest request = new CommunityBoardRequest("제목1", "내용1",
        CommunityCategory.NORMAL);

    Optional<User> findUser = userRepository.findById(1L);
    List<MultipartFile> multipartFiles = new ArrayList<>();

    MockMultipartFile multipartFile = new MockMultipartFile("files", "imageFile.jpeg", "image/jpeg",
        "<<jpeg data>>".getBytes());

    multipartFiles.add(multipartFile);

    //when
    communityBoardService.createCommunityBoard(request, multipartFiles, findUser.get());

//    Optional<CommunityBoard> findCommunityBoard = communityBoardRepository.findById(1L);
//
//    CommunityBoardResponse communityBoardResponse = communityBoardService.getCommunityBoard(
//        1L, findUser.get());

//    List<BoardImage> boardImageList = boardImageRepository.findAllByBoardId(1L);
//    List<String> imagePaths = new ArrayList<>();
//    for (BoardImage boardImage : boardImageList) {
//      imagePaths.add(boardImage.getImagePath());
//    }
  }

//  @AfterAll
//  public void afterAll() throws IOException {
//    Optional<User> findUser = userRepository.findById(1L);
//    communityBoardService.deleteCommunityBoard(1L, findUser.get());
//  }


  List<String> getOriginImagePaths() {
    List<BoardImage> originBoardImageList = boardImageRepository.findAllByBoardId(1L);
    List<String> originImagePaths = new ArrayList<>();
    for (BoardImage boardImage : originBoardImageList) {
      originImagePaths.add(boardImage.getImagePath());
    }
    return originImagePaths;
  }

  @Test
  @DisplayName("잡담게시글 생성(이미지) & 단건 조회")
  @Order(1)
  void createCommunityBoard() throws IOException {

    //given
    CommunityBoardRequest request = new CommunityBoardRequest("제목2", "내용2",
        CommunityCategory.NORMAL);
//    User user1 = new User("user1", "pas12!@", "userNickname", "aaa@a.com");
//    userRepository.save(user1);
    Optional<User> findUser = userRepository.findById(1L);
    Optional<User> readUser = userRepository.findById(2L);
    List<MultipartFile> multipartFiles = new ArrayList<>();

    MockMultipartFile multipartFile = new MockMultipartFile("files", "imageFile.jpeg", "image/jpeg",
        "<<jpeg data>>".getBytes());

    multipartFiles.add(multipartFile);

    //when
    communityBoardService.createCommunityBoard(request, multipartFiles, findUser.get());

    Optional<CommunityBoard> findCommunityBoard = communityBoardRepository.findById(2L);

    boardLikeService.addBoardLike(readUser.get().getId(), findCommunityBoard.get().getId());

    CommunityBoardResponse communityBoardResponse = communityBoardService.getCommunityBoard(
        2L, readUser.get());

    List<BoardImage> boardImageList = boardImageRepository.findAllByBoardId(2L);
    List<String> imagePaths = new ArrayList<>();
    for (BoardImage boardImage : boardImageList) {
      imagePaths.add(boardImage.getImagePath());
    }

    //then
    assertThat(communityBoardResponse.getTitle()).isEqualTo(findCommunityBoard.get().getTitle());
    assertThat(communityBoardResponse.getContent()).isEqualTo(
        findCommunityBoard.get().getContent());
    assertThat(communityBoardResponse.getCommunityCategory()).isEqualTo(
        findCommunityBoard.get().getCommunityCategory());
    assertThat(communityBoardResponse.getImagePaths()).isEqualTo(imagePaths);
    assertThat(communityBoardResponse.getNickname()).isEqualTo(findUser.get().getNickname());
    assertThat(communityBoardResponse.getBoardLike()).isEqualTo(1);
    assertThat(communityBoardResponse.isHasLike()).isEqualTo(true);

  }

  @Test
  @DisplayName("잡담게시글 생성(이미지 빈파일) & 단건 조회")
  @Order(2)
  void createEmptyImageCommunityBoard() throws IOException {

    //given
    CommunityBoardRequest request = new CommunityBoardRequest("제목3", "내용3",
        CommunityCategory.NORMAL);
//    User user1 = new User("user1", "pas12!@", "userNickname", "aaa@a.com");
//
//    userRepository.save(user1);
    Optional<User> findUser = userRepository.findById(1L);
    Optional<User> readUser = userRepository.findById(2L);

    List<MultipartFile> multipartFiles = new ArrayList<>();

    //when
    communityBoardService.createCommunityBoard(request, multipartFiles, findUser.get());

    Optional<CommunityBoard> findCommunityBoard = communityBoardRepository.findById(3L);

    boardLikeService.addBoardLike(readUser.get().getId(), findCommunityBoard.get().getId());

    CommunityBoardResponse communityBoardResponse = communityBoardService.getCommunityBoard(
        findCommunityBoard.get().getId(), readUser.get());

    List<BoardImage> boardImageList = boardImageRepository.findAllByBoardId(
        findCommunityBoard.get().getId());
    List<String> imagePaths = new ArrayList<>();
    for (BoardImage boardImage : boardImageList) {
      imagePaths.add(boardImage.getImagePath());
    }

    //then
    assertThat(communityBoardResponse.getTitle()).isEqualTo(findCommunityBoard.get().getTitle());
    assertThat(communityBoardResponse.getContent()).isEqualTo(
        findCommunityBoard.get().getContent());
    assertThat(communityBoardResponse.getCommunityCategory()).isEqualTo(
        findCommunityBoard.get().getCommunityCategory());
    assertThat(communityBoardResponse.getImagePaths()).isEqualTo(imagePaths);
    assertThat(communityBoardResponse.getNickname()).isEqualTo(findUser.get().getNickname());
    assertThat(communityBoardResponse.getBoardLike()).isEqualTo(1);
    assertThat(communityBoardResponse.isHasLike()).isEqualTo(true);

  }

  @Test
  @DisplayName("잡담게시글 수정(이미지 빈파일)")
  @Order(3)
  void updateEmptyImageCommunityBoard() throws IOException {
//
//    List<BoardImage> originBoardImageList = boardImageRepository.findAllByBoardId(1L);
//    List<String> originImagePaths = new ArrayList<>();
//    for (BoardImage boardImage : originBoardImageList) {
//      originImagePaths.add(boardImage.getImagePath());
//    }

    //와 여기 쓰니까 이상하게 먹혔던 거였네..
//    Optional<CommunityBoard> findBoard = communityBoardRepository.findById(1L);
    Optional<User> findUser = userRepository.findById(1L);

    CommunityBoardRequest communityBoardRequest = new CommunityBoardRequest("수정1", "수정1",
        CommunityCategory.GRADE);

    List<MultipartFile> multipartFiles = new ArrayList<>();

    communityBoardService.updateCommunityBoard(1L, multipartFiles, communityBoardRequest,
        findUser.get());

    Optional<CommunityBoard> updatedBoard = communityBoardRepository.findById(1L);
    //이것도 위에 있다가 위치 바꾸니 먹히네..
    List<String> originImagePaths = getOriginImagePaths();
    List<BoardImage> boardImageList = boardImageRepository.findAllByBoardId(1L);
    List<String> imagePaths = new ArrayList<>();
    for (BoardImage boardImage : boardImageList) {
      imagePaths.add(boardImage.getImagePath());
    }

    assertThat(updatedBoard.get().getTitle()).isEqualTo(communityBoardRequest.getTitle());
    assertThat(updatedBoard.get().getContent()).isEqualTo(communityBoardRequest.getContent());
    assertThat(updatedBoard.get().getCommunityCategory()).isEqualTo(
        communityBoardRequest.getCommunityCategory());
    assertThat(originImagePaths).isEqualTo(imagePaths);
  }

  @Test
  @DisplayName("잡담게시글 수정(이미지 파일)")
  @Order(4)
  void updateCommunityBoard() throws IOException {

//    List<BoardImage> originBoardImageList = boardImageRepository.findAllByBoardId(1L);
//    List<String> originImagePaths = new ArrayList<>();
//    for (BoardImage boardImage : originBoardImageList) {
//      originImagePaths.add(boardImage.getImagePath());
//    }

//    Optional<CommunityBoard> findBoard = communityBoardRepository.findById(1L);
    Optional<User> findUser = userRepository.findById(1L);

    CommunityBoardRequest communityBoardRequest = new CommunityBoardRequest("수정12", "수정12",
        CommunityCategory.GRADE);

    List<MultipartFile> multipartFiles = new ArrayList<>();

    MockMultipartFile multipartFile = new MockMultipartFile("files", "imageFile(수정).jpeg",
        "image/jpeg",
        "<<jpeg data>>".getBytes());
    multipartFiles.add(multipartFile);

    //위치 조심..
    List<String> originImagePaths = getOriginImagePaths();

    communityBoardService.updateCommunityBoard(1L, multipartFiles, communityBoardRequest,
        findUser.get());
    Optional<CommunityBoard> updatedBoard = communityBoardRepository.findById(1L);

    List<BoardImage> boardImageList = boardImageRepository.findAllByBoardId(1L);
    List<String> imagePaths = new ArrayList<>();
    for (BoardImage boardImage : boardImageList) {
      imagePaths.add(boardImage.getImagePath());
    }

    assertThat(updatedBoard.get().getTitle()).isEqualTo(communityBoardRequest.getTitle());
    assertThat(updatedBoard.get().getContent()).isEqualTo(communityBoardRequest.getContent());
    assertThat(updatedBoard.get().getCommunityCategory()).isEqualTo(
        communityBoardRequest.getCommunityCategory());
    assertThat(originImagePaths).isNotEqualTo(imagePaths);

  }

  @Test
  @DisplayName("잡담글 삭제")
  @Order(5)
  void deleteCommunityBoard() {
    Optional<User> findUser = userRepository.findById(1L);
//    System.out.println(communityBoardRepository.findById(1L));
    assertThat(communityBoardRepository.existsBoardById(1L)).isTrue();
    communityBoardService.deleteCommunityBoard(1L, findUser.get());
    assertThat(communityBoardRepository.existsBoardById(1L)).isFalse();
  }


}