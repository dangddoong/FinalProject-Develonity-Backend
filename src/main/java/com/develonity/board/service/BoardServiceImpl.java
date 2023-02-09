package com.develonity.board.service;

import com.develonity.board.dto.QuestionBoardRequest;
import com.develonity.board.dto.QuestionBoardResponse;
import com.develonity.board.dto.QuestionBoardSearch;
import com.develonity.board.entity.Board;
import com.develonity.board.entity.QuestionBoard;
import com.develonity.board.repository.BoardRepository;
import com.develonity.user.entity.User;
import com.develonity.user.entity.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class BoardServiceImpl implements BoardService {

  private final BoardRepository boardRepository;

//  private final UserService userService;

  //질문 게시글 생성
  @Override
  @Transactional
  public QuestionBoardResponse createBoard(QuestionBoardRequest request, User user) {
    QuestionBoard questionBoard = new QuestionBoard(user.getId(), request.getTitle(),
        request.getContent(), request.getCategory(), request.getImageUrl(), request.getPoint());
    boardRepository.save(questionBoard);
    //userService.deductPoint(request.getPrizePoint); -> 유저서비스에서 질문자가 걸어놓은 포인트 차감되는 메소드 필요. 메소드 명은 알아서
    return new QuestionBoardResponse(questionBoard, user);
  }

  //질문 게시글 수정
  @Override
  @Transactional
  public void updateBoard(Long boardId, QuestionBoardRequest request, User user) {
    QuestionBoard questionBoard = (QuestionBoard) boardRepository.findById(boardId)
        .orElseThrow(/*CustomException.NotFoundException::new*/);
    if (isAuthorized(user, questionBoard)) {
      questionBoard.updateBoard(request.getTitle(), request.getContent(), request.getCategory(),
          request.getImageUrl());
      boardRepository.save(questionBoard);
    } else {
      /*throw new CustomException.NotAuthorityException()*/
    }
  }

  //질문 게시글 삭제
  @Override
  @Transactional
  public void deleteBoard(Long boardId, User user) {
    QuestionBoard questionBoard = (QuestionBoard) boardRepository.findById(boardId)
        .orElseThrow(/*CustomException.NotFoundException::new*/);
    if (isAuthorized(user, questionBoard)) {
      boardRepository.deleteById(boardId);
    } else {
      /*throw new CustomException.NotAuthorityException()*/
    }
  }

  // 질문 게시글 선택 조회
  @Override
  @Transactional(readOnly = true)
  public QuestionBoardResponse getQuestionBoard(Long boardId, User user) {

    QuestionBoard questionBoard = boardRepository.findBoardById(boardId);
    return new QuestionBoardResponse(questionBoard, user);
  }


  //질문 게시글 전체 조회
  @Override
  @Transactional(readOnly = true)
  public Page<QuestionBoardResponse> getQuetionBoardPage(User user,
      QuestionBoardSearch questionBoardSearch) {

    Page<QuestionBoard> questionBoardPage = boardRepository.findByCategoryAndTitleContaining(
        questionBoardSearch.getSearch(), questionBoardSearch.toPageable(),
        questionBoardSearch.getCategory());
    return questionBoardPage.map(questionBoard -> new QuestionBoardResponse(questionBoard, user));

  }

  //해당 유저가 맞는지 또는 관리자 권한을 가졌는지 확인하는 메소드
  @Override
  public boolean isAuthorized(User user, Board board) {
    return user.getId().equals(board.getUserId()) || user.getUserRole() == UserRole.ADMIN;
  }

}