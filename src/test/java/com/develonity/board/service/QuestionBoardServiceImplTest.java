package com.develonity.board.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.develonity.board.dto.BoardSearchCond;
import com.develonity.board.dto.PageDto;
import com.develonity.board.dto.QuestionBoardRequest;
import com.develonity.board.dto.QuestionBoardResponse;
import com.develonity.board.dto.QuestionBoardSearchCond;
import com.develonity.board.dto.QuestionBoardUpdateRequest;
import com.develonity.board.entity.BoardImage;
import com.develonity.board.entity.QuestionBoard;
import com.develonity.board.entity.QuestionCategory;
import com.develonity.board.repository.BoardImageRepository;
import com.develonity.board.repository.BoardLikeRepository;
import com.develonity.board.repository.QuestionBoardRepository;
import com.develonity.user.entity.User;
import com.develonity.user.repository.UserRepository;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

@SpringBootTest
//@TestInstance(Lifecycle.PER_CLASS)
class QuestionBoardServiceImplTest {

  @Autowired
  private QuestionBoardRepository questionBoardRepository;

  @Autowired
  private BoardLikeService boardLikeService;

  @Autowired
  private QuestionBoardService questionBoardService;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private BoardImageRepository boardImageRepository;
  @Autowired
  private BoardLikeRepository boardLikeRepository;

  @BeforeEach
  void AllDeleteBefore() {
    questionBoardRepository.deleteAll();
  }
  @AfterEach
  void AllDeleteAfter() {
    questionBoardRepository.deleteAll();
    boardLikeRepository.deleteAll();
    boardImageRepository.deleteAll();
  }

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

//    questionBoardRepository.delete(createQuestionBoard);
  }

  @Test
  @DisplayName("질문게시글 생성(이미지 빈파일) + 단건 조회")
  void createEmptyImageQuestionBoard() throws IOException {
    //given
    QuestionBoardRequest request = new QuestionBoardRequest("제목6", "내용6",
        90, QuestionCategory.AI);

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

//    questionBoardRepository.delete(createQuestionBoard);
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

//    questionBoardRepository.delete(updateQuestionBoard);
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

//    questionBoardRepository.delete(updateQuestionBoard);

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


  @Test
  @DisplayName("질문게시글 전체조회 + 검색")
  void getAllQuestionBoard() {

    PageDto pageDto = PageDto.builder().page(1).size(10).build();
    //질문글 생성(2개)
    QuestionBoard q = QuestionBoard.builder().questionCategory(QuestionCategory.AI)
        .userId(1L)
        .title("안녕")
        .content("하세요")
        .build();

    questionBoardRepository.save(q);

    QuestionBoard q2 = QuestionBoard.builder().questionCategory(QuestionCategory.FRONTEND)
        .userId(2L)
        .title("반갑")
        .content("습니다")
        .build();

    questionBoardRepository.save(q2);

    QuestionBoardSearchCond cond = QuestionBoardSearchCond.builder().build();
//    BoardSearchCond cond = BoardSearchCond.builder().build();

    //전체 조회(2개)
    Page<QuestionBoardResponse> responsesAll = questionBoardService.searchQuestionBoardByCond(
        cond, pageDto);
    assertThat(responsesAll.getTotalElements()).isEqualTo(2);

//제목 검색
    QuestionBoardSearchCond condTitle = QuestionBoardSearchCond.builder()
        .questionCategory(QuestionCategory.AI)
        .title("안녕")
//        .content("하세요")
//        .nickname("d")
//        .boardSort(BoardSort.EMPTY)
//        .sortDirection(SortDirection.DESC)
        .build();

    Page<QuestionBoardResponse> responsesTitle = questionBoardService.searchQuestionBoardByCond(
        condTitle, pageDto);

    assertThat(responsesTitle.getTotalElements()).isEqualTo(1);

    //내용 검색
    QuestionBoardSearchCond condContent = QuestionBoardSearchCond.builder()
        .content("하세요")
        .build();

    Page<QuestionBoardResponse> responsesContent = questionBoardService.searchQuestionBoardByCond(
        condContent, pageDto);

    assertThat(responsesContent.getTotalElements()).isEqualTo(1);

    //닉네임 검색
    QuestionBoardSearchCond condNickname = QuestionBoardSearchCond.builder()
        .nickname("당")
        .build();

    Page<QuestionBoardResponse> responsesNickname = questionBoardService.searchQuestionBoardByCond(
        condNickname, pageDto);

    assertThat(responsesNickname.getTotalElements()).isEqualTo(1);

    //카테고리 검색
    QuestionBoardSearchCond condCategory = QuestionBoardSearchCond.builder()
        .questionCategory(QuestionCategory.AI).build();
    Page<QuestionBoardResponse> responsesCategory = questionBoardService.searchQuestionBoardByCond(
        condCategory, pageDto);
    assertThat(responsesCategory.getTotalElements()).isEqualTo(1);

  }
}
