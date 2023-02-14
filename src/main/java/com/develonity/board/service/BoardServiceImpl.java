package com.develonity.board.service;

import com.develonity.board.dto.QuestionBoardPage;
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
public class BoardServiceImpl implements BoardService {

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
  public QuestionBoardResponse createBoard(QuestionBoardRequest request, User user) {
    QuestionBoard questionBoard = QuestionBoard.builder()
        .userId(user.getId())
        .title(request.getTitle())
        .content(request.getContent())
        .category(request.getCategory())
        .prizePoint(request.getPoint())
        .build();

    questionBoardRepository.save(questionBoard);
    //userService.deductPoint(request.getPrizePoint); -> 유저서비스에서 질문자가 걸어놓은 포인트 차감되는 메소드 필요. 메소드 명은 알아서
    return new QuestionBoardResponse(questionBoard, user,
        boardLikeService.countLike(questionBoard.getId()));
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
    QuestionBoard questionBoard = getQuestionBoard(boardId);
    checkUser(questionBoard, user.getId());
    questionBoard.updateBoard(request.getTitle(), request.getContent(), request.getCategory());
    questionBoardRepository.save(questionBoard);
  }

  //질문 게시글 삭제
  @Override
  @Transactional
  public void deleteBoard(Long boardId, User user) {
    QuestionBoard questionBoard = getQuestionBoard(boardId);
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
  
    QuestionBoard questionBoard = getQuestionBoard(boardId);
    /*islike메소드 트루인지 포스인지 확인하고*/

    return new QuestionBoardResponse(questionBoard, user,
        boardLikeService.countLike(questionBoard.getId())/*, 트루포스*/);
  }


  //질문 게시글 전체 조회
  @Override
  @Transactional(readOnly = true)
  public Page<QuestionBoardResponse> getQuetionBoardPage(User user,
      QuestionBoardPage questionBoardPage) {

//    Page<QuestionBoard> questionBoardPages = boardRepository.findByTitleContaining(
//        /*questionBoardPage.getCategory(),*/ questionBoardPage.getTitle(),
//        questionBoardPage.toPageable());
//

    Page<QuestionBoard> questionBoardPages = questionBoardRepository.findByCategoryAndTitleContaining(
        questionBoardPage.getCategory(), questionBoardPage.getTitle(),
        questionBoardPage.toPageable());

    return questionBoardPages.map(questionBoard -> new QuestionBoardResponse(questionBoard, user,
        boardLikeService.countLike(questionBoard.getId())));

  }

  private QuestionBoard getQuestionBoard(Long boardId) {
    return questionBoardRepository.findById(boardId)
        .orElseThrow(() -> new CustomException(ExceptionStatus.BOARD_IS_NOT_EXIST));
  }

  private void checkUser(QuestionBoard questionBoard, Long userId) {
    if (!questionBoard.isWriter(userId)) {
      throw new CustomException(ExceptionStatus.BOARD_USER_NOT_MATCH);
    }
  }
}