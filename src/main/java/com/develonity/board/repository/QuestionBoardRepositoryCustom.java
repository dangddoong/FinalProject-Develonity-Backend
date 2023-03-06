package com.develonity.board.repository;

import com.develonity.board.dto.BoardSearchCond;
import com.develonity.board.dto.PageDto;
import com.develonity.board.dto.QuestionBoardResponse;
import com.develonity.board.dto.QuestionBoardSearchCond;
import java.util.List;
import org.springframework.data.domain.Page;

public interface QuestionBoardRepositoryCustom {

  Page<QuestionBoardResponse> searchQuestionBoard(QuestionBoardSearchCond questionBoardSearchCond,
      PageDto pageDto);

  Page<QuestionBoardResponse> searchMyQuestionBoard(QuestionBoardSearchCond questionBoardSearchCond,
      PageDto pageDto, Long userId);

  List<QuestionBoardResponse> QuestionBoardOrderByLikes(QuestionBoardSearchCond cond);
}
