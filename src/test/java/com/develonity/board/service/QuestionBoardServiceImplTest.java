package com.develonity.board.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.develonity.board.dto.QuestionBoardRequest;
import com.develonity.board.dto.QuestionBoardResponse;
import com.develonity.board.dto.QuestionBoardUpdateRequest;
import com.develonity.board.entity.BoardImage;
import com.develonity.board.entity.QuestionBoard;
import com.develonity.board.entity.QuestionCategory;
import com.develonity.board.repository.BoardImageRepository;
import com.develonity.board.repository.QuestionBoardRepository;
import com.develonity.common.jwt.JwtUtil;
import com.develonity.user.entity.User;
import com.develonity.user.repository.UserRepository;
import com.develonity.user.service.UserService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.Rollback;
import org.springframework.web.multipart.MultipartFile;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class QuestionBoardServiceImplTest {

  @Autowired
  private QuestionBoardRepository questionBoardRepository;

  @Autowired
  private BoardLikeService boardLikeService;

  @Autowired
  private UserService userService;

  @Autowired
  private QuestionBoardService questionBoardService;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private JwtUtil jwtUtil;

  @Autowired
  private BoardImageRepository boardImageRepository;

  @BeforeEach
  public void beforeEach() throws IOException {
    QuestionBoardRequest request = new QuestionBoardRequest("제목1", "내용1",
        5, QuestionCategory.BACKEND);

    Optional<User> findUser = userRepository.findById(1L);
    List<MultipartFile> multipartFiles = new ArrayList<>();

    MockMultipartFile multipartFile = new MockMultipartFile("files", "imageFile.jpeg", "image/jpeg",
        "<<jpeg data>>".getBytes());

    multipartFiles.add(multipartFile);

    //when
    questionBoardService.createQuestionBoard(request, multipartFiles, findUser.get());
  }

  List<String> getOriginImagePaths() {
    List<BoardImage> originBoardImageList = boardImageRepository.findAllByBoardId(1L);
    List<String> originImagePaths = new ArrayList<>();
    for (BoardImage boardImage : originBoardImageList) {
      originImagePaths.add(boardImage.getImagePath());
    }
    return originImagePaths;
  }

  @Test
  @DisplayName("질문게시글 생성(이미지) & 단건 조회")
  @Order(1)
  @Rollback
  void createQuestionBoard() throws IOException {
    //given
    QuestionBoardRequest request = new QuestionBoardRequest("제목1", "내용1",
        100, QuestionCategory.AI);
//    User user1 = new User("user1", "pas12!@", "userNickname", "aaa@a.com");
//    userRepository.save(user1);
    Optional<User> findUser = userRepository.findById(1L);
    Optional<User> readUser = userRepository.findById(2L);
    List<MultipartFile> multipartFiles = new ArrayList<>();

    MockMultipartFile multipartFile = new MockMultipartFile("files", "imageFile.jpeg", "image/jpeg",
        "<<jpeg data>>".getBytes());

    multipartFiles.add(multipartFile);

    //when
    questionBoardService.createQuestionBoard(request, multipartFiles, findUser.get());

    Optional<QuestionBoard> findQuestionBoard = questionBoardRepository.findById(2L);
    boardLikeService.addBoardLike(readUser.get().getId(), findQuestionBoard.get().getId());
    QuestionBoardResponse questionBoardResponse = questionBoardService.getQuestionBoard(
        findQuestionBoard.get().getId(), readUser.get());

    List<BoardImage> boardImageList = boardImageRepository.findAllByBoardId(
        findQuestionBoard.get().getId());
    List<String> imagePaths = new ArrayList<>();
    for (BoardImage boardImage : boardImageList) {
      imagePaths.add(boardImage.getImagePath());
    }

    //then
    assertThat(questionBoardResponse.getTitle()).isEqualTo(findQuestionBoard.get().getTitle());
    assertThat(questionBoardResponse.getContent()).isEqualTo(
        findQuestionBoard.get().getContent());
    assertThat(questionBoardResponse.getQuestionCategory()).isEqualTo(
        findQuestionBoard.get().getQuestionCategory());
    assertThat(questionBoardResponse.getImagePaths()).isEqualTo(imagePaths);
    assertThat(questionBoardResponse.getNickname()).isEqualTo(findUser.get().getNickname());
    assertThat(questionBoardResponse.getPrizePoint()).isEqualTo(
        findQuestionBoard.get().getPrizePoint());
//    assertThat(questionBoardResponse.getBoardLike()).isEqualTo(1);
//    assertThat(questionBoardResponse.getHasLike()).isEqualTo(true);
  }

  @Test
  @DisplayName("질문게시글 생성(이미지 빈파일) + 단건 조회")
  @Order(2)
  @Rollback
  void createEmptyImageQuestionBoard() throws IOException {
    //given
    QuestionBoardRequest request = new QuestionBoardRequest("제목1", "내용1",
        90, QuestionCategory.AI);
//    User user1 = new User("user1", "pas12!@", "userNickname", "aaa@a.com");
//
//    userRepository.save(user1);
    Optional<User> findUser = userRepository.findById(1L);
    Optional<User> readUser = userRepository.findById(2L);

    List<MultipartFile> multipartFiles = new ArrayList<>();

    //when
    questionBoardService.createQuestionBoard(request, multipartFiles, findUser.get());

    Optional<QuestionBoard> findQuestionBoard = questionBoardRepository.findById(3L);
    boardLikeService.addBoardLike(readUser.get().getId(), findQuestionBoard.get().getId());

    QuestionBoardResponse questionBoardResponse = questionBoardService.getQuestionBoard(
        findQuestionBoard.get().getId(), readUser.get());

    List<BoardImage> boardImageList = boardImageRepository.findAllByBoardId(
        questionBoardResponse.getId());
    List<String> imagePaths = new ArrayList<>();
    for (BoardImage boardImage : boardImageList) {
      imagePaths.add(boardImage.getImagePath());
    }

    //then
    assertThat(questionBoardResponse.getTitle()).isEqualTo(findQuestionBoard.get().getTitle());
    assertThat(questionBoardResponse.getContent()).isEqualTo(
        findQuestionBoard.get().getContent());
    assertThat(questionBoardResponse.getQuestionCategory()).isEqualTo(
        findQuestionBoard.get().getQuestionCategory());
    assertThat(questionBoardResponse.getImagePaths()).isEqualTo(imagePaths);
    assertThat(questionBoardResponse.getNickname()).isEqualTo(findUser.get().getNickname());
    assertThat(questionBoardResponse.getPrizePoint()).isEqualTo(
        findQuestionBoard.get().getPrizePoint());
    assertThat(questionBoardResponse.getBoardLike()).isEqualTo(1);
    assertThat(questionBoardResponse.getHasLike()).isEqualTo(true);
  }

  @Test
  @DisplayName("질문게시글 수정(이미지 빈파일)")
  @Order(3)
  @Rollback
  void updateEmptyImageQuestionBoard() throws IOException {
    Optional<User> findUser = userRepository.findById(1L);

    QuestionBoardUpdateRequest questionBoardRequest = new QuestionBoardUpdateRequest("수정1", "수정1",
        QuestionCategory.FRONTEND);

    List<MultipartFile> multipartFiles = new ArrayList<>();

    questionBoardService.updateQuestionBoard(1L, multipartFiles, questionBoardRequest,
        findUser.get());

    Optional<QuestionBoard> updatedBoard = questionBoardRepository.findById(1L);
    List<String> originImagePaths = getOriginImagePaths();
    List<BoardImage> boardImageList = boardImageRepository.findAllByBoardId(1L);
    List<String> imagePaths = new ArrayList<>();
    for (BoardImage boardImage : boardImageList) {
      imagePaths.add(boardImage.getImagePath());
    }

    assertThat(updatedBoard.get().getTitle()).isEqualTo(questionBoardRequest.getTitle());
    assertThat(updatedBoard.get().getContent()).isEqualTo(questionBoardRequest.getContent());
    assertThat(updatedBoard.get().getQuestionCategory()).isEqualTo(
        questionBoardRequest.getQuestionCategory());
    assertThat(originImagePaths).isEqualTo(imagePaths);
  }

  @Test
  @DisplayName("질문게시글 수정(이미지 파일)")
  @Order(4)
  @Rollback
  void updateQuestionBoard() throws IOException {

    Optional<User> findUser = userRepository.findById(1L);

    QuestionBoardUpdateRequest questionBoardUpdateRequest = new QuestionBoardUpdateRequest("수정1",
        "수정1",
        QuestionCategory.FRONTEND);

    List<MultipartFile> multipartFiles = new ArrayList<>();

    MockMultipartFile multipartFile = new MockMultipartFile("files", "imageFile(수정).jpeg",
        "image/jpeg",
        "<<jpeg data>>".getBytes());
    multipartFiles.add(multipartFile);

    //위치 조심..
    List<String> originImagePaths = getOriginImagePaths();

    questionBoardService.updateQuestionBoard(1L, multipartFiles, questionBoardUpdateRequest,
        findUser.get());
    Optional<QuestionBoard> updatedBoard = questionBoardRepository.findById(1L);

    List<BoardImage> boardImageList = boardImageRepository.findAllByBoardId(1L);
    List<String> imagePaths = new ArrayList<>();
    for (BoardImage boardImage : boardImageList) {
      imagePaths.add(boardImage.getImagePath());
    }

    assertThat(updatedBoard.get().getTitle()).isEqualTo(questionBoardUpdateRequest.getTitle());
    assertThat(updatedBoard.get().getContent()).isEqualTo(questionBoardUpdateRequest.getContent());
    assertThat(updatedBoard.get().getQuestionCategory()).isEqualTo(
        questionBoardUpdateRequest.getQuestionCategory());
    assertThat(originImagePaths).isNotEqualTo(imagePaths);

  }

  @Test
  @DisplayName("질문게시글 삭제")
  @Order(5)
  @Rollback
  void deleteQuestionBoard() {
    Optional<User> findUser = userRepository.findById(1L);
    assertThat(questionBoardRepository.existsBoardById(1L)).isTrue();
    questionBoardService.deleteQuestionBoard(1L, findUser.get());
    assertThat(questionBoardRepository.existsBoardById(1L)).isFalse();
  }

  @Test
  void getQuestionBoard() {
  }

  @Test
  void getQuestionBoardPage() {
  }

  @Test
  void getTestQuestionBoardPage() {
  }

  @Test
  void searchQuestionBoardByCond() {
  }

  @Test
  void questionBoardOrderBy() {
  }
}