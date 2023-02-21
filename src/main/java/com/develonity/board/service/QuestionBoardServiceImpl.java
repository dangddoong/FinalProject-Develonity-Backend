package com.develonity.board.service;

import com.develonity.board.dto.BoardPage;
import com.develonity.board.dto.QuestionBoardRequest;
import com.develonity.board.dto.QuestionBoardResponse;
import com.develonity.board.entity.BoardImage;
import com.develonity.board.entity.QuestionBoard;
import com.develonity.board.repository.BoardImageRepository;
import com.develonity.board.repository.QuestionBoardRepository;
import com.develonity.comment.service.CommentService;
import com.develonity.common.aws.AwsS3Service;
import com.develonity.common.exception.CustomException;
import com.develonity.common.exception.ExceptionStatus;
import com.develonity.user.entity.User;
import com.develonity.user.service.UserService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
public class QuestionBoardServiceImpl implements QuestionBoardService {

  private final QuestionBoardRepository questionBoardRepository;

  private final BoardLikeService boardLikeService;

  private final BoardImageRepository boardImageRepository;

  private final CommentService commentService;

  private final AwsS3Service awsS3Service;

  private final UserService userService;

  private final ScrapService scrapService;


  //질문 게시글 생성(+이미지)
  @Override
  @Transactional
  public void createQuestionBoard(QuestionBoardRequest request,
      List<MultipartFile> multipartFiles,
      User user) throws IOException {
    QuestionBoard questionBoard = QuestionBoard.builder()
        .userId(user.getId())
        .title(request.getTitle())
        .content(request.getContent())
        .prizePoint(request.getPoint())
        .questionCategory(request.getQuestionCategory())
        .build();
    questionBoardRepository.save(questionBoard);
    if (multipartFiles != null) {
      upload(multipartFiles, questionBoard);
    }
    userService.subtractGiftPoint(questionBoard.getPrizePoint(), user);
  }

  //질문 게시글 수정(+이미지)

  @Override
  @Transactional
  public void updateQuestionBoard(Long boardId, List<MultipartFile> multipartFiles,
      QuestionBoardRequest request, User user) throws IOException {
    QuestionBoard questionBoard = getQuestionBoardAndCheck(boardId);
    checkUser(questionBoard, user.getId());
    if (multipartFiles != null) {
      deleteBoardImages(boardId);
      upload(multipartFiles, questionBoard);
    }
    questionBoard.updateBoard(request.getTitle(), request.getContent());
    questionBoardRepository.save(questionBoard);
  }

  //질문 게시글 삭제
  @Override
  @Transactional
  public void deleteQuestionBoard(Long boardId, User user) {
    QuestionBoard questionBoard = getQuestionBoardAndCheck(boardId);
    checkUser(questionBoard, user.getId());
    boardLikeService.deleteLike(boardId);
    deleteBoardImages(boardId);
    commentService.deleteCommentsByBoardId(boardId);
    scrapService.deleteScraps(boardId);
    questionBoardRepository.deleteById(boardId);
  }


  // 질문 게시글 선택 조회
  @Override
  @Transactional(readOnly = true)
  public QuestionBoardResponse getQuestionBoard(Long boardId, User user) {

    QuestionBoard questionBoard = getQuestionBoardAndCheck(boardId);

    List<BoardImage> boardImageList = boardImageRepository.findAllByBoardId(boardId);

    List<String> imagePaths = new ArrayList<>();
    for (BoardImage boardImage : boardImageList) {
      imagePaths.add(boardImage.getImagePath());
    }

    Long boardUserId = questionBoard.getUserId();
    String nickname = getNickname(boardUserId);
    boolean hasLike = boardLikeService.existsLikesBoardIdAndUserId(boardId, user.getId());
    return new QuestionBoardResponse(questionBoard, nickname, countLike(boardId), hasLike,
        imagePaths);
  }


  //질문 게시글 전체 조회
  @Override
  @Transactional(readOnly = true)
  public Page<QuestionBoardResponse> getQuetionBoardPage(User user,
      BoardPage questionBoardPage) {

    Page<QuestionBoard> questionBoardPages = questionBoardRepository.findByQuestionCategoryAndTitleContainingOrContentContaining(
        questionBoardPage.getQuestionCategory(), questionBoardPage.getTitle(),
        questionBoardPage.getContent(),
        questionBoardPage.toPageable());

    return questionBoardPages.map(
        questionBoard -> QuestionBoardResponse.toQuestionBoardResponse(questionBoard,
            getNicknameByQuestionBoard(questionBoard)));

  }

  //테스트용전체조회
  @Override
  public Page<QuestionBoardResponse> getTestQuetionBoardPage(User user,
      BoardPage questionBoardPage) {

    Page<QuestionBoard> questionBoardPages = questionBoardRepository.findByQuestionCategory(
        questionBoardPage.getQuestionCategory(),
        questionBoardPage.toPageable());

    return questionBoardPages.map(
        questionBoard -> QuestionBoardResponse.toQuestionBoardResponse(questionBoard,
            getNicknameByQuestionBoard(questionBoard)));
  }

  @Override
  public int countLike(Long boardId) {
    return boardLikeService.countLikes(boardId);
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

  @Override
  public boolean getQuestionBoardAndCheckSameUser(Long boardId, Long userId) {
    return questionBoardRepository.findBoardById(boardId).getUserId().equals(userId);
  }

  @Override
  @Transactional
  public void upload(List<MultipartFile> multipartFiles, QuestionBoard questionBoard)
      throws IOException {

    List<String> uploadImagePaths = new ArrayList<>();
    String dir = "/board/questionImage";
    boolean exists = false;
    for (MultipartFile multipartFile : multipartFiles) {
      if (!multipartFile.isEmpty()) {
        exists = true;
      }
    }
    if (exists) {
      uploadImagePaths = awsS3Service.upload(multipartFiles, dir);
    }

    for (String imagePath : uploadImagePaths) {
      BoardImage boardImage = new BoardImage(imagePath, questionBoard.getId());
      boardImageRepository.save(boardImage);
    }
  }


  @Override
  @Transactional
  public void deleteBoardImages(Long boardId) {
    List<BoardImage> boardImages = boardImageRepository.findAllByBoardId(boardId);

    List<String> imagePaths = new ArrayList<>();

    for (BoardImage boardImage : boardImages) {
      imagePaths.add(boardImage.getImagePath());
    }
    for (String imagePath : imagePaths) {
      awsS3Service.deleteFile(imagePath);
    }
    boardImageRepository.deleteAllByBoardId(boardId);
  }
}


