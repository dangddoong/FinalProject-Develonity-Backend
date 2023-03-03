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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
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

//  @BeforeAll
//  public void beforeAll() throws IOException {
//
//    QuestionBoardRequest request = new QuestionBoardRequest("제목4", "내용4",
//        5, QuestionCategory.BACKEND);
//
//    Optional<User> findUser = userRepository.findById(1L);
//    List<MultipartFile> multipartFiles = new ArrayList<>();
//
//    MockMultipartFile multipartFile = new MockMultipartFile("files", "imageFile.jpeg", "image/jpeg",
//        "<<jpeg data>>".getBytes());
//
//    multipartFiles.add(multipartFile);
//
//    //when
//    QuestionBoard firstQuestionBoard = questionBoardService.createQuestionBoard(request,
//        multipartFiles, findUser.get());
//    System.out.println(firstQuestionBoard.getId());
//  }

//  @AfterAll
//  public void afterAll() throws IOException {
//    Optional<User> findUser = userRepository.findById(1L);
//    questionBoardService.deleteQuestionBoard(1L, findUser.get());
//  }
//
//  List<String> getOriginImagePaths() {
//    List<BoardImage> originBoardImageList = boardImageRepository.findAllByBoardId(1L);
//    List<String> originImagePaths = new ArrayList<>();
//    for (BoardImage boardImage : originBoardImageList) {
//      originImagePaths.add(boardImage.getImagePath());
//    }
//    return originImagePaths;
//  }

  @Test
  @DisplayName("질문게시글 생성(이미지) & 단건 조회")
  void createQuestionBoard() throws IOException {
    //given
    QuestionBoardRequest request = new QuestionBoardRequest("제목5", "내용5",
        100, QuestionCategory.AI);

    Optional<User> findUser = userRepository.findById(1L);
    Optional<User> readUser = userRepository.findById(2L);
    List<MultipartFile> multipartFiles = new ArrayList<>();

    MockMultipartFile multipartFile = new MockMultipartFile("files", "imageFile.jpeg", "image/jpeg",
        "<<jpeg data>>".getBytes());

    multipartFiles.add(multipartFile);

    //when
    QuestionBoard createQuestionBoard = questionBoardService.createQuestionBoard(request,
        multipartFiles, findUser.get());

    boardLikeService.addBoardLike(readUser.get().getId(), createQuestionBoard.getId());
    QuestionBoardResponse questionBoardResponse = questionBoardService.getQuestionBoard(
        createQuestionBoard.getId(), readUser.get());

    List<BoardImage> boardImageList = boardImageRepository.findAllByBoardId(
        createQuestionBoard.getId());
    List<String> imagePaths = new ArrayList<>();
    for (BoardImage boardImage : boardImageList) {
      imagePaths.add(boardImage.getImagePath());
    }

    //then
    assertThat(questionBoardResponse.getTitle()).isEqualTo(createQuestionBoard.getTitle());
    assertThat(questionBoardResponse.getContent()).isEqualTo(
        createQuestionBoard.getContent());
    assertThat(questionBoardResponse.getQuestionCategory()).isEqualTo(
        createQuestionBoard.getQuestionCategory());
    assertThat(questionBoardResponse.getImagePaths()).isEqualTo(imagePaths);
    assertThat(questionBoardResponse.getNickname()).isEqualTo(findUser.get().getNickname());
    assertThat(questionBoardResponse.getPrizePoint()).isEqualTo(
        createQuestionBoard.getPrizePoint());
    assertThat(questionBoardResponse.getBoardLike()).isEqualTo(1);
    assertThat(questionBoardResponse.getHasLike()).isEqualTo(true);
  }

  @Test
  @DisplayName("질문게시글 생성(이미지 빈파일) + 단건 조회")
  void createEmptyImageQuestionBoard() throws IOException {
    //given
    QuestionBoardRequest request = new QuestionBoardRequest("제목6", "내용6",
        90, QuestionCategory.AI);
//    User user1 = new User("user1", "pas12!@", "userNickname", "aaa@a.com");
//
//    userRepository.save(user1);
    Optional<User> findUser = userRepository.findById(1L);
    Optional<User> readUser = userRepository.findById(2L);

    List<MultipartFile> multipartFiles = new ArrayList<>();

    //when
    QuestionBoard createQuestionBoard = questionBoardService.createQuestionBoard(request,
        multipartFiles, findUser.get());

    boardLikeService.addBoardLike(readUser.get().getId(), createQuestionBoard.getId());

    QuestionBoardResponse questionBoardResponse = questionBoardService.getQuestionBoard(
        createQuestionBoard.getId(), readUser.get());

    List<BoardImage> boardImageList = boardImageRepository.findAllByBoardId(
        questionBoardResponse.getId());
    List<String> imagePaths = new ArrayList<>();
    for (BoardImage boardImage : boardImageList) {
      imagePaths.add(boardImage.getImagePath());
    }

    //then
    assertThat(questionBoardResponse.getTitle()).isEqualTo(createQuestionBoard.getTitle());
    assertThat(questionBoardResponse.getContent()).isEqualTo(
        createQuestionBoard.getContent());
    assertThat(questionBoardResponse.getQuestionCategory()).isEqualTo(
        createQuestionBoard.getQuestionCategory());
    assertThat(questionBoardResponse.getImagePaths()).isEqualTo(imagePaths);
    assertThat(questionBoardResponse.getNickname()).isEqualTo(findUser.get().getNickname());
    assertThat(questionBoardResponse.getPrizePoint()).isEqualTo(
        createQuestionBoard.getPrizePoint());
    assertThat(questionBoardResponse.getBoardLike()).isEqualTo(1);
    assertThat(questionBoardResponse.getHasLike()).isEqualTo(true);
  }

  @Test
  @DisplayName("질문게시글 수정(이미지 빈파일)")
  void updateEmptyImageQuestionBoard() throws IOException {
    Optional<User> findUser = userRepository.findById(1L);

// 질문게시글 생성
    QuestionBoardRequest request = new QuestionBoardRequest("제목4", "내용4",
        5, QuestionCategory.BACKEND);
    List<MultipartFile> multipartFiles = new ArrayList<>();
    MockMultipartFile multipartFile = new MockMultipartFile("files", "imageFile.jpeg", "image/jpeg",
        "<<jpeg data>>".getBytes());
    multipartFiles.add(multipartFile);

    QuestionBoard createdQuestionBoard = questionBoardService.createQuestionBoard(request,
        multipartFiles, findUser.get());

    List<BoardImage> originBoardImageList = boardImageRepository.findAllByBoardId(
        createdQuestionBoard.getId());
    List<String> originImagePaths = new ArrayList<>();
    for (BoardImage boardImage : originBoardImageList) {
      originImagePaths.add(boardImage.getImagePath());
    }

    //질문 게시글 수정
    QuestionBoardUpdateRequest questionBoardRequest = new QuestionBoardUpdateRequest("수정4", "수정4",
        QuestionCategory.FRONTEND);

    //이미지는 업로드 x한 경우
    QuestionBoard updateQuestionBoard = questionBoardService.updateQuestionBoard(
        createdQuestionBoard.getId(), null,
        questionBoardRequest,
        findUser.get());

    //수정 후 이미지 리스트
    List<BoardImage> boardImageList = boardImageRepository.findAllByBoardId(
        updateQuestionBoard.getId());
    List<String> imagePaths = new ArrayList<>();
    for (BoardImage boardImage : boardImageList) {
      imagePaths.add(boardImage.getImagePath());
    }

    assertThat(updateQuestionBoard.getTitle()).isEqualTo(questionBoardRequest.getTitle());
    assertThat(updateQuestionBoard.getContent()).isEqualTo(questionBoardRequest.getContent());
    assertThat(updateQuestionBoard.getQuestionCategory()).isEqualTo(
        questionBoardRequest.getQuestionCategory());
    assertThat(originImagePaths).isEqualTo(imagePaths);
  }

  @Test
  @DisplayName("질문게시글 수정(이미지 파일)")
  void updateQuestionBoard() throws IOException {

    // 질문게시글 생성

    Optional<User> findUser = userRepository.findById(1L);
    QuestionBoardRequest request = new QuestionBoardRequest("제목4", "내용4",
        5, QuestionCategory.BACKEND);
    List<MultipartFile> multipartFiles = new ArrayList<>();
    MockMultipartFile multipartFile = new MockMultipartFile("files", "imageFile.jpeg", "image/jpeg",
        "<<jpeg data>>".getBytes());
    multipartFiles.add(multipartFile);

    QuestionBoard createdQuestionBoard = questionBoardService.createQuestionBoard(request,
        multipartFiles, findUser.get());

    List<BoardImage> originBoardImageList = boardImageRepository.findAllByBoardId(
        createdQuestionBoard.getId());
    List<String> originImagePaths = new ArrayList<>();
    for (BoardImage boardImage : originBoardImageList) {
      originImagePaths.add(boardImage.getImagePath());
    }

    //질문 게시글 수정(이미지파일 넣는 경우)
    QuestionBoardUpdateRequest questionBoardUpdateRequest = new QuestionBoardUpdateRequest("수정42",
        "수정42",
        QuestionCategory.FRONTEND);

    List<MultipartFile> updateMultipartFiles = new ArrayList<>();
    MockMultipartFile updateMultipartFile = new MockMultipartFile("files", "imageFile(수정).jpeg",
        "image/jpeg",
        "<<jpeg data>>".getBytes());
    updateMultipartFiles.add(updateMultipartFile);

    QuestionBoard updateQuestionBoard = questionBoardService.updateQuestionBoard(
        createdQuestionBoard.getId(), updateMultipartFiles, questionBoardUpdateRequest,
        findUser.get());

    //수정 후 이미지 파일
    List<BoardImage> boardImageList = boardImageRepository.findAllByBoardId(
        updateQuestionBoard.getId());
    List<String> imagePaths = new ArrayList<>();
    for (BoardImage boardImage : boardImageList) {
      imagePaths.add(boardImage.getImagePath());
    }

    assertThat(updateQuestionBoard.getTitle()).isEqualTo(questionBoardUpdateRequest.getTitle());
    assertThat(updateQuestionBoard.getContent()).isEqualTo(questionBoardUpdateRequest.getContent());
    assertThat(updateQuestionBoard.getQuestionCategory()).isEqualTo(
        questionBoardUpdateRequest.getQuestionCategory());
    assertThat(originImagePaths).isNotEqualTo(imagePaths);

  }

  @Test
  @DisplayName("질문게시글 삭제")
  void deleteQuestionBoard() throws IOException {

    //질문게시글 생성
    Optional<User> findUser = userRepository.findById(1L);
    QuestionBoardRequest request = new QuestionBoardRequest("제목4", "내용4",
        5, QuestionCategory.BACKEND);
    QuestionBoard createdQuestionBoard = questionBoardService.createQuestionBoard(request,
        null, findUser.get());

    assertThat(questionBoardRepository.existsBoardById(createdQuestionBoard.getId())).isTrue();
    questionBoardService.deleteQuestionBoard(createdQuestionBoard.getId(), findUser.get());
    assertThat(questionBoardRepository.existsBoardById(createdQuestionBoard.getId())).isFalse();
  }
}