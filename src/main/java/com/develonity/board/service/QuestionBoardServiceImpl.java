package com.develonity.board.service;

import com.develonity.board.dto.BoardPage;
import com.develonity.board.dto.QuestionBoardRequest;
import com.develonity.board.dto.QuestionBoardResponse;
import com.develonity.board.entity.QuestionBoard;
import com.develonity.board.repository.QuestionBoardRepository;
import com.develonity.common.exception.CustomException;
import com.develonity.common.exception.ExceptionStatus;
import com.develonity.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class QuestionBoardServiceImpl implements QuestionBoardService {

  private final QuestionBoardRepository questionBoardRepository;

  private final BoardLikeService boardLikeService;
//
//  private final BoardImageRepository boardImageRepository;
//
//  private final AwsS3Service awsS3Service;

//  private final UserService userService;

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
//  public QuestionBoardResponse createBoard(QuestionBoardRequest request,
//      List<MultipartFile> multipartFiles,
//      User user) throws IOException {
//    List<String> imagePaths = awsS3Service.upload(multipartFiles);
//    QuestionBoard questionBoard = QuestionBoard.builder()
//        .userId(user.getId())
//        .title(request.getTitle())
//        .content(request.getContent())
//        .category(request.getCategory())
//        .prizePoint(request.getPoint())
//        .build();
//
//    for (String imagePath : imagePaths) {
//      BoardImage boardImage = new BoardImage(imagePath, questionBoard);
//      boardImageRepository.save(boardImage);
//    }
//
//    questionBoardRepository.save(questionBoard);
//    //userService.deductPoint(request.getPrizePoint); -> 유저서비스에서 질문자가 걸어놓은 포인트 차감되는 메소드 필요. 메소드 명은 알아서
//    return new QuestionBoardResponse(questionBoard, user,
//        boardLikeService.countLike(questionBoard.getId()));
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

  //질문 게시글 삭제
  @Override
  @Transactional
  public void deleteBoard(Long boardId, User user) {
    QuestionBoard questionBoard = getQuestionBoardAndCheck(boardId);
    checkUser(questionBoard, user.getId());
    if (boardLikeService.isExistLikes(boardId)) {
      boardLikeService.deleteLike(boardId);
    }
    questionBoardRepository.deleteById(boardId);

  }

  // 질문 게시글 선택 조회
  @Override
  @Transactional(readOnly = true)
  public QuestionBoardResponse getQuestionBoard(Long boardId, User user) {

    QuestionBoard questionBoard = getQuestionBoardAndCheck(boardId);
    /*islike메소드 트루인지 포스인지 확인하고*/
    boolean isLike = boardLikeService.isLike(boardId, user.getId());
    return new QuestionBoardResponse(questionBoard, user, countLike(boardId), isLike);
  }


  //질문 게시글 전체 조회
  @Override
  @Transactional(readOnly = true)
  public Page<QuestionBoardResponse> getQuetionBoardPage(User user,
      BoardPage questionBoardPage) {

//    Page<QuestionBoard> questionBoardPages = boardRepository.findByTitleContaining(
//        /*questionBoardPage.getCategory(),*/ questionBoardPage.getTitle(),
//        questionBoardPage.toPageable());
//

    Page<QuestionBoard> questionBoardPages = questionBoardRepository.findBySubCategoryAndTitleContainingOrContentContaining(
        questionBoardPage.getSubCategory(), questionBoardPage.getTitle(),
        questionBoardPage.getContent(),
        questionBoardPage.toPageable());

    return questionBoardPages.map(
        questionBoard -> QuestionBoardResponse.toQuestionBoardResponse(questionBoard, user));

  }

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
  public int countLike(Long boardId) {
    return boardLikeService.countLike(boardId);
  }

}