package com.develonity.board.repository;

import com.develonity.board.dto.BoardSearchCond;
import com.develonity.board.dto.CommunityBoardResponse;
import com.develonity.board.dto.PageDto;
import org.springframework.data.domain.Page;

public interface CommunityBoardRepositoryCustom {

  Page<CommunityBoardResponse> searchCommunityBoard(BoardSearchCond cond,
      PageDto pageDto);


}
