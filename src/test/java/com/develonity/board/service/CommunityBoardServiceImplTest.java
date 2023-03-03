package com.develonity.board.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.develonity.board.dto.BoardSearchCond;
import com.develonity.board.dto.CommunityBoardRequest;
import com.develonity.board.dto.CommunityBoardResponse;
import com.develonity.board.dto.PageDto;
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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
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

//  @BeforeAll
//  public void beforeAll() throws IOException {
//    CommunityBoardRequest request = new CommunityBoardRequest("제목1", "내용1",
//        CommunityCategory.NORMAL);
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
//    communityBoardService.createCommunityBoard(request, multipartFiles, findUser.get());

//    List<BoardImage> boardImageList = boardImageRepository.findAllByBoardId(1L);
//    List<String> imagePaths = new ArrayList<>();
//    for (BoardImage boardImage : boardImageList) {
//      imagePaths.add(boardImage.getImagePath());
//    }
//  }

//  @AfterAll
//  public void afterAll() throws IOException {
//    Optional<User> findUser = userRepository.findById(1L);
//    communityBoardService.deleteCommunityBoard(1L, findUser.get());
//  }
//
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
  @DisplayName("잡담게시글 생성(이미지) & 단건 조회")
  void createCommunityBoard() throws IOException {

    //given
    CommunityBoardRequest request = new CommunityBoardRequest("제목2", "내용2",
        CommunityCategory.NORMAL);

    Optional<User> findUser = userRepository.findById(1L);
    Optional<User> readUser = userRepository.findById(2L);
    List<MultipartFile> multipartFiles = new ArrayList<>();

    MockMultipartFile multipartFile = new MockMultipartFile("files", "imageFile.jpeg", "image/jpeg",
        "<<jpeg data>>".getBytes());

    multipartFiles.add(multipartFile);

    //when
    CommunityBoard createCommunityBoard = communityBoardService.createCommunityBoard(request,
        multipartFiles, findUser.get());

    boardLikeService.addBoardLike(readUser.get().getId(), createCommunityBoard.getId());

    CommunityBoardResponse communityBoardResponse = communityBoardService.getCommunityBoard(
        createCommunityBoard.getId(), readUser.get());

    List<BoardImage> boardImageList = boardImageRepository.findAllByBoardId(
        createCommunityBoard.getId());
    List<String> imagePaths = new ArrayList<>();
    for (BoardImage boardImage : boardImageList) {
      imagePaths.add(boardImage.getImagePath());
    }
    //then

    assertThat(communityBoardResponse.getTitle()).isEqualTo(createCommunityBoard.getTitle());
    assertThat(communityBoardResponse.getContent()).isEqualTo(
        createCommunityBoard.getContent());
    assertThat(communityBoardResponse.getCommunityCategory()).isEqualTo(
        createCommunityBoard.getCommunityCategory());
    assertThat(communityBoardResponse.getImagePaths()).isEqualTo(imagePaths);
    assertThat(communityBoardResponse.getNickname()).isEqualTo(findUser.get().getNickname());
    assertThat(communityBoardResponse.getBoardLike()).isEqualTo(1);
    assertThat(communityBoardResponse.isHasLike()).isEqualTo(true);

  }

  @Test
  @DisplayName("잡담게시글 생성(이미지 빈파일) & 단건 조회")
  void createEmptyImageCommunityBoard() throws IOException {

    //given
    CommunityBoardRequest request = new CommunityBoardRequest("제목3", "내용3",
        CommunityCategory.NORMAL);

    Optional<User> findUser = userRepository.findById(1L);
    Optional<User> readUser = userRepository.findById(2L);

    List<MultipartFile> multipartFiles = new ArrayList<>();

    //when
    CommunityBoard createCommunityBoard = communityBoardService.createCommunityBoard(request,
        multipartFiles, findUser.get());

    boardLikeService.addBoardLike(readUser.get().getId(), createCommunityBoard.getId());

    CommunityBoardResponse communityBoardResponse = communityBoardService.getCommunityBoard(
        createCommunityBoard.getId(), readUser.get());

    List<BoardImage> boardImageList = boardImageRepository.findAllByBoardId(
        createCommunityBoard.getId());
    List<String> imagePaths = new ArrayList<>();
    for (BoardImage boardImage : boardImageList) {
      imagePaths.add(boardImage.getImagePath());
    }

    //then
    assertThat(communityBoardResponse.getTitle()).isEqualTo(createCommunityBoard.getTitle());
    assertThat(communityBoardResponse.getContent()).isEqualTo(
        createCommunityBoard.getContent());
    assertThat(communityBoardResponse.getCommunityCategory()).isEqualTo(
        createCommunityBoard.getCommunityCategory());
    assertThat(communityBoardResponse.getImagePaths()).isEqualTo(imagePaths);
    assertThat(communityBoardResponse.getNickname()).isEqualTo(findUser.get().getNickname());
    assertThat(communityBoardResponse.getBoardLike()).isEqualTo(1);
    assertThat(communityBoardResponse.isHasLike()).isEqualTo(true);

  }

  @Test
  @DisplayName("잡담게시글 수정(이미지 빈파일)")
  void updateEmptyImageCommunityBoard() throws IOException {

    Optional<User> findUser = userRepository.findById(1L);

    //잡담글 생성
    CommunityBoardRequest request = new CommunityBoardRequest("제목3", "내용3",
        CommunityCategory.NORMAL);
    List<MultipartFile> multipartFiles = new ArrayList<>();
    MockMultipartFile multipartFile = new MockMultipartFile("files", "imageFile.jpeg", "image/jpeg",
        "<<jpeg data>>".getBytes());
    multipartFiles.add(multipartFile);

    CommunityBoard createCommunityBoard = communityBoardService.createCommunityBoard(request,
        multipartFiles, findUser.get());

    List<BoardImage> originBoardImageList = boardImageRepository.findAllByBoardId(
        createCommunityBoard.getId());
    List<String> originImagePaths = new ArrayList<>();
    for (BoardImage boardImage : originBoardImageList) {
      originImagePaths.add(boardImage.getImagePath());
    }
    //잡담글 수정
    CommunityBoardRequest communityBoardRequest = new CommunityBoardRequest("수정1", "수정1",
        CommunityCategory.GRADE);
    //이미지는 업로드 x한 경우
    CommunityBoard updateCommunityBoard = communityBoardService.updateCommunityBoard(
        createCommunityBoard.getId(),
        null, communityBoardRequest,
        findUser.get());

    //수정 후 이미지 리스트
    List<BoardImage> boardImageList = boardImageRepository.findAllByBoardId(
        updateCommunityBoard.getId());
    List<String> imagePaths = new ArrayList<>();
    for (BoardImage boardImage : boardImageList) {
      imagePaths.add(boardImage.getImagePath());
    }

    assertThat(updateCommunityBoard.getTitle()).isEqualTo(communityBoardRequest.getTitle());
    assertThat(updateCommunityBoard.getContent()).isEqualTo(communityBoardRequest.getContent());
    assertThat(updateCommunityBoard.getCommunityCategory()).isEqualTo(
        communityBoardRequest.getCommunityCategory());
    assertThat(originImagePaths).isEqualTo(imagePaths);
  }

  @Test
  @DisplayName("잡담게시글 수정(이미지 파일)")
  void updateCommunityBoard() throws IOException {
//잡담글 생성
    Optional<User> findUser = userRepository.findById(1L);
    CommunityBoardRequest request = new CommunityBoardRequest("제목3", "내용3",
        CommunityCategory.NORMAL);
    List<MultipartFile> multipartFiles = new ArrayList<>();
    MockMultipartFile multipartFile = new MockMultipartFile("files", "imageFile.jpeg", "image/jpeg",
        "<<jpeg data>>".getBytes());
    multipartFiles.add(multipartFile);

    CommunityBoard createCommunityBoard = communityBoardService.createCommunityBoard(request,
        multipartFiles, findUser.get());

    List<BoardImage> originBoardImageList = boardImageRepository.findAllByBoardId(
        createCommunityBoard.getId());
    List<String> originImagePaths = new ArrayList<>();
    for (BoardImage boardImage : originBoardImageList) {
      originImagePaths.add(boardImage.getImagePath());
    }
//잡담글 수정(이미지 파일 넣는 경우)

    CommunityBoardRequest communityBoardRequest = new CommunityBoardRequest("수정12", "수정12",
        CommunityCategory.GRADE);

    List<MultipartFile> updateMultipartFiles = new ArrayList<>();

    MockMultipartFile updateMultipartFile = new MockMultipartFile("files", "imageFile(수정).jpeg",
        "image/jpeg",
        "<<jpeg data>>".getBytes());
    updateMultipartFiles.add(updateMultipartFile);

    CommunityBoard updateCommunityBoard = communityBoardService.updateCommunityBoard(
        createCommunityBoard.getId(), updateMultipartFiles, communityBoardRequest,
        findUser.get());
//수정 후 이미지 파일
    List<BoardImage> boardImageList = boardImageRepository.findAllByBoardId(1L);
    List<String> imagePaths = new ArrayList<>();
    for (BoardImage boardImage : boardImageList) {
      imagePaths.add(boardImage.getImagePath());
    }

    assertThat(updateCommunityBoard.getTitle()).isEqualTo(communityBoardRequest.getTitle());
    assertThat(updateCommunityBoard.getContent()).isEqualTo(communityBoardRequest.getContent());
    assertThat(updateCommunityBoard.getCommunityCategory()).isEqualTo(
        communityBoardRequest.getCommunityCategory());
    assertThat(originImagePaths).isNotEqualTo(imagePaths);

  }

  @Test
  @DisplayName("잡담글 삭제")
  void deleteCommunityBoard() throws IOException {
    Optional<User> findUser = userRepository.findById(1L);
    //잡담게시글 생성
    CommunityBoardRequest request = new CommunityBoardRequest("제목3", "내용3",
        CommunityCategory.NORMAL);
    CommunityBoard communityBoard = communityBoardService.createCommunityBoard(
        request,
        null, findUser.get());

//    System.out.println(communityBoardRepository.findById(1L));
    assertThat(communityBoardRepository.existsBoardById(communityBoard.getId())).isTrue();
    communityBoardService.deleteCommunityBoard(communityBoard.getId(), findUser.get());
    assertThat(communityBoardRepository.existsBoardById(communityBoard.getId())).isFalse();
  }

  @Test
  @DisplayName("잡담글 전체조회")
  void getAllCommunityBoard() {

    PageDto pageDto = PageDto.builder().page(1).size(10).build();
//    BoardSearchCond boardSearchCond = BoardSearchCond.builder()
//        .communityCategory(CommunityCategory.NORMAL)
//        .title("제목")
////        .content("1")
////        .nickname("d")
////        .boardSort(BoardSort.EMPTY)
////        .sortDirection(SortDirection.DESC)
//        .build();
//

//
//    Page<CommunityBoardResponse> responses = communityBoardService.searchCommunityBoardByCond(
//        boardSearchCond, pageDto);
//
//    assertThat(responses.getTotalElements()).isEqualTo(2);

    CommunityBoard c = CommunityBoard.builder().communityCategory(CommunityCategory.GRADE)
        .userId(1L)
        .title("메롱")
        .content("메롱")
        .build();

    communityBoardRepository.save(c);

    BoardSearchCond boardSearchCond2 = BoardSearchCond.builder()
        .communityCategory(CommunityCategory.GRADE)
//        .title("수정")
        .content("메롱")
//        .nickname("d")
//        .boardSort(BoardSort.EMPTY)
//        .sortDirection(SortDirection.DESC)
        .build();

    Page<CommunityBoardResponse> responses2 = communityBoardService.searchCommunityBoardByCond(
        boardSearchCond2, pageDto);

    assertThat(responses2.getTotalElements()).isEqualTo(1);
  }

}