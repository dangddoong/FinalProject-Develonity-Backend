package com.develonity.board.service;

import com.develonity.board.dto.QuestionBoardRequest;
import com.develonity.board.dto.QuestionBoardResponse;
import com.develonity.board.dto.QuestionBoardSearch;
import com.develonity.board.entity.Board;
import com.develonity.user.entity.User;
import org.springframework.data.domain.Page;

public interface BoardService {

  QuestionBoardResponse createBoard(QuestionBoardRequest request, User user);

  void updateBoard(Long boardId, QuestionBoardRequest request, User user);

  void deleteBoard(Long boardId, User user);

  boolean isAuthorized(User user, Board board);

  Page<QuestionBoardResponse> getQuetionBoardPage(User user,
      QuestionBoardSearch questionBoardSearch);

  QuestionBoardResponse getQuestionBoard(Long boardId, User user);
}
