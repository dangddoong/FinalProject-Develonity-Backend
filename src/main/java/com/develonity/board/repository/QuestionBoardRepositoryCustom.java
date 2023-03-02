package com.develonity.board.repository;

import com.develonity.board.dto.BoardSearchCond;
import com.develonity.board.dto.PageDto;
import com.develonity.board.dto.QuestionBoardResponse;
import java.util.List;
import org.springframework.data.domain.Page;

public interface QuestionBoardRepositoryCustom {

  Page<QuestionBoardResponse> searchQuestionBoard(BoardSearchCond cond,
      PageDto pageDto);

  //  List<QuestionBoardResponse> QuestionBoardOrderByLikes(BoardSearchCond cond);
  List<QuestionBoardResponse> QuestionBoardOrderByLikes();
}
