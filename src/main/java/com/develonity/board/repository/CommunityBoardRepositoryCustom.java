package com.develonity.board.repository;

import com.develonity.board.dto.BoardSearchCond;
import com.develonity.board.dto.CommunityBoardResponse;
import com.develonity.board.dto.CommunityBoardSearchCond;
import com.develonity.board.dto.PageDto;
import org.springframework.data.domain.Page;

public interface CommunityBoardRepositoryCustom {

  Page<CommunityBoardResponse> searchCommunityBoard(CommunityBoardSearchCond cond,
      PageDto pageDto);

  Page<CommunityBoardResponse> searchMyCommunityBoard(CommunityBoardSearchCond cond, PageDto pageDto, Long userId);
}
