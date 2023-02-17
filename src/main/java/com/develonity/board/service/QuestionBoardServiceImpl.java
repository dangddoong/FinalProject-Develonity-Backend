package com.develonity.board.service;

import com.develonity.board.dto.BoardPage;
import com.develonity.board.dto.QuestionBoardRequest;
import com.develonity.board.dto.QuestionBoardResponse;
import com.develonity.board.entity.QuestionBoard;
import com.develonity.board.repository.BoardImageRepository;
import com.develonity.board.repository.QuestionBoardRepository;
import com.develonity.comment.service.CommentService;
import com.develonity.common.exception.CustomException;
import com.develonity.common.exception.ExceptionStatus;
import com.develonity.user.entity.User;
import com.develonity.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class QuestionBoardServiceImpl implements QuestionBoardService {

  private final QuestionBoardRepository questionBoardRepository;

  private final BoardLikeService boardLikeService;

  private final BoardImageRepository boardImageRepository;

  private final CommentService commentService;

//  private final AwsS3Service awsS3Service;

  private final UserService userService;

  //질문 게시글 생성
  @Override
  @Transactional
  public void createBoard(QuestionBoardRequest request, User user) {
    QuestionBoard questionBoard = QuestionBoard.builder()
        .userId(user.getId())
        .title(request.getTitle())
        .content(request.getContent())
        .category(request.getCategory())
        .prizePoint(request.getPoint())
        .subCategory(request.getSubCategory())
        .build();

    questionBoardRepository.save(questionBoard);
    //userService.deductPoint(request.getPrizePoint); -> 유저서비스에서 질문자가 걸어놓은 포인트 차감되는 메소드 필요. 메소드 명은 알아서
  }

  //질문 게시글 생성(+이미지)
//  @Override
//  @Transactional
//  public void createBoard(QuestionBoardRequest request,
//      List<MultipartFile> multipartFiles,
//      User user) throws IOException {
//    QuestionBoard questionBoard = QuestionBoard.builder()
//        .userId(user.getId())
//        .title(request.getTitle())
//        .content(request.getContent())
//        .category(request.getCategory())
//        .prizePoint(request.getPoint())
//        .build();
//
//    upload(multipartFiles, questionBoard);
//    questionBoardRepository.save(questionBoard);
//    //userService.deductPoint(request.getPrizePoint); -> 유저서비스에서 질문자가 걸어놓은 포인트 차감되는 메소드 필요. 메소드 명은 알아서
//  }

  //질문 게시글 수정
  @Override
  @Transactional
  public void updateBoard(Long boardId, QuestionBoardRequest request, User user) {
    QuestionBoard questionBoard = getQuestionBoardAndCheck(boardId);
    checkUser(questionBoard, user.getId());
    questionBoard.updateBoard(request.getTitle(), request.getContent(), request.getCategory());
    questionBoardRepository.save(questionBoard);
  }
  //질문 게시글 수정(+이미지)
//  @Override
//  @Transactional
//  public void updateBoard(Long boardId, List<MultipartFile> multipartFiles,
//      QuestionBoardRequest request, User user) throws IOException {
//    QuestionBoard questionBoard = getQuestionBoardAndCheck(boardId);
//    checkUser(questionBoard, user.getId());
//    for (MultipartFile multipartFile : multipartFiles) {
//      if (!multipartFile.isEmpty()) {
//        deleteBoardImages(boardId);
//        upload(multipartFiles, questionBoard);
//      } else {
//        upload(multipartFiles, questionBoard);
//      }
//    }
//    questionBoard.updateBoard(request.getTitle(), request.getContent(), request.getCategory());
//    questionBoardRepository.save(questionBoard);
//  }


  //질문 게시글 삭제
  @Override
  @Transactional
  public void deleteBoard(Long boardId, User user) {
    QuestionBoard questionBoard = getQuestionBoardAndCheck(boardId);
    checkUser(questionBoard, user.getId());
    if (boardLikeService.isExistLikes(boardId)) {
      boardLikeService.deleteLike(boardId);
    }
//    deleteBoardImages(boardId);
//    commentService.deleteCommentByBoardId(boardId);
    questionBoardRepository.deleteById(boardId);
  }


  // 질문 게시글 선택 조회
  @Override
  @Transactional(readOnly = true)
  public QuestionBoardResponse getQuestionBoard(Long boardId, User user) {

    QuestionBoard questionBoard = getQuestionBoardAndCheck(boardId);

    Long userId = questionBoard.getUserId();
    String nickname = getNickname(userId);
    boolean isLike = boardLikeService.isLike(boardId, user.getId());
    return new QuestionBoardResponse(questionBoard, nickname, countLike(boardId), isLike);
  }


  //질문 게시글 전체 조회
  @Override
  @Transactional(readOnly = true)
  public Page<QuestionBoardResponse> getQuetionBoardPage(User user,
      BoardPage questionBoardPage) {

    Page<QuestionBoard> questionBoardPages = questionBoardRepository.findBySubCategoryAndTitleContainingOrContentContaining(
        questionBoardPage.getSubCategory(), questionBoardPage.getTitle(),
        questionBoardPage.getContent(),
        questionBoardPage.toPageable());

    return questionBoardPages.map(
        questionBoard -> QuestionBoardResponse.toQuestionBoardResponse(questionBoard,
            getNicknameByQuestionBoard(questionBoard)));

  }

  @Override
  public int countLike(Long boardId) {
    return boardLikeService.countLike(boardId);
  }

  //게시글 가져오기 + 있는지 확인
  @Override
  public QuestionBoard getQuestionBoardAndCheck(Long boardId) {
    return questionBoardRepository.findById(boardId)
        .orElseThrow(() -> new CustomException(ExceptionStatus.BOARD_IS_NOT_EXIST));
  }

  @Override
  public void checkUser(QuestionBoard questionBoard, Long userId) {
    if (!questionBoard.isWriter(userId)) {
      throw new CustomException(ExceptionStatus.BOARD_USER_NOT_MATCH);
    }
  }

  @Override
  public boolean isExistBoard(Long boardId) {
    return questionBoardRepository.existsBoardById(boardId);
  }

  @Override
  public String getNickname(Long userId) {
    return userService.getProfile(userId).getNickname();
  }

  @Override
  public String getNicknameByQuestionBoard(QuestionBoard questionBoard) {
    return userService.getProfile(questionBoard.getUserId()).getNickname();
  }

//  @Override
//  public void upload(List<MultipartFile> multipartFiles, QuestionBoard questionBoard)
//      throws IOException {
//
//    List<String> uploadImagePaths = new ArrayList<>();
//    int checkNumber = 0;
//    for (MultipartFile multipartFile : multipartFiles) {
//      if (!multipartFile.isEmpty()) {
//        checkNumber = 1;
//      }
//    }
//    if (checkNumber == 1) {
//      uploadImagePaths = awsS3Service.upload(multipartFiles);
//    }
//
//    for (String imagePath : uploadImagePaths) {
//      BoardImage boardImage = new BoardImage(imagePath, questionBoard);
//      boardImageRepository.save(boardImage);
//    }
//  }
//
//  @Override
//  public void deleteBoardImages(Long boardId) {
//    List<BoardImage> boardImages = boardImageRepository.findAllByBoardId(boardId);
//
//    List<String> imagePaths = new ArrayList<>();
//
//    for (BoardImage boardImage : boardImages) {
//      imagePaths.add(boardImage.getImagePath());
//    }
//    for (String imagePath : imagePaths) {
//      awsS3Service.deleteFile(imagePath);
//    }
//    boardImageRepository.deleteBoardImageByBoardId(boardId);
//  }

//나중에 다시 보기
  //  @Override
//  public void upload(List<MultipartFile> multipartFiles, QuestionBoard questionBoard)
//      throws IOException {
//    List<String> uploadImagePaths = new ArrayList<>();
//    for (MultipartFile multipartFile : multipartFiles) {
//      if (!multipartFile.isEmpty()) {
//
//      }
//      uploadImagePaths = awsS3Service.upload(multipartFiles);
//    }
//
//    for (String imagePath : uploadImagePaths) {
//      BoardImage boardImage = new BoardImage(imagePath, questionBoard);
//      boardImageRepository.save(boardImage);
//    }
//  }
//
//
}




