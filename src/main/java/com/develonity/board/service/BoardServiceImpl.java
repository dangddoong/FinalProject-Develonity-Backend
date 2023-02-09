package com.develonity.board.service;

import com.develonity.board.dto.QuestionBoardPage;
import com.develonity.board.dto.QuestionBoardRequest;
import com.develonity.board.dto.QuestionBoardResponse;
import com.develonity.board.entity.QuestionBoard;
import com.develonity.board.repository.BoardRepository;
import com.develonity.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class BoardServiceImpl implements BoardService {

  private final BoardRepository boardRepository;

  private final BoardLikeService boardLikeService;

//  private final UserService userService;

  //질문 게시글 생성
  @Override
  @Transactional
  public QuestionBoardResponse createBoard(QuestionBoardRequest request, User user) {
//    QuestionBoard questionBoard = new QuestionBoard(user.getId(), request.getTitle(),
//        request.getContent(), request.getCategory(), request.getImageUrl(), request.getPoint());
    QuestionBoard questionBoard = QuestionBoard.builder()
        .userId(user.getId())
        .title(request.getTitle())
        .content(request.getContent())
        .category(request.getCategory())
        .imageUrl(request.getImageUrl())
        .prizePoint(request.getPoint())
        .build();

    boardRepository.save(questionBoard);
    //userService.deductPoint(request.getPrizePoint); -> 유저서비스에서 질문자가 걸어놓은 포인트 차감되는 메소드 필요. 메소드 명은 알아서
    return new QuestionBoardResponse(questionBoard, user,
        boardLikeService.countLike(questionBoard.getId()));
  }

  //질문 게시글 수정
  @Override
  @Transactional
  public void updateBoard(Long boardId, QuestionBoardRequest request, User user) {
    QuestionBoard questionBoard = (QuestionBoard) boardRepository.findById(boardId)
        .orElseThrow(/*CustomException.NotFoundException::new*/);

    if (!questionBoard.isWriter(questionBoard.getId())) {
      /*throw new CustomException.NotAuthorityException()*/
    }
    questionBoard.updateBoard(request.getTitle(), request.getContent(), request.getCategory(),
        request.getImageUrl());
    boardRepository.save(questionBoard);
  }

  //질문 게시글 삭제
  @Override
  @Transactional
  public void deleteBoard(Long boardId, User user) {
    QuestionBoard questionBoard = (QuestionBoard) boardRepository.findById(boardId)
        .orElseThrow(/*CustomException.NotFoundException::new*/);
    if (!questionBoard.isWriter(questionBoard.getId())) {
      /*throw new CustomException.NotAuthorityException()*/
    }
    boardRepository.deleteById(boardId);
  }

  // 질문 게시글 선택 조회
  @Override
  @Transactional(readOnly = true)
  public QuestionBoardResponse getQuestionBoard(Long boardId, User user) {

    QuestionBoard questionBoard = boardRepository.findBoardById(boardId);
    return new QuestionBoardResponse(questionBoard, user,
        boardLikeService.countLike(questionBoard.getId()));
  }


  //질문 게시글 전체 조회
  @Override
  @Transactional(readOnly = true)
  public Page<QuestionBoardResponse> getQuetionBoardPage(User user,
      QuestionBoardPage questionBoardSearch) {

    Page<QuestionBoard> questionBoardPage = boardRepository.findByCategoryAndTitleContaining(
        questionBoardSearch.getSearch(), questionBoardSearch.toPageable(),
        questionBoardSearch.getCategory());

    return questionBoardPage.map(questionBoard -> new QuestionBoardResponse(questionBoard, user,
        boardLikeService.countLike(questionBoard.getId())));

  }

  //보드 존재하는지 확인하는 메소드. 댓글에서 사용하시면 될 것 같습니다.
  @Override
  public boolean isExistBoard(Long boardId) {
    return boardRepository.existsBoardById(boardId);
  }

}