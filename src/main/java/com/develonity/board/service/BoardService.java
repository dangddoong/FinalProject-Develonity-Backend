package com.develonity.board.service;

import com.develonity.board.dto.BoardPage;
import com.develonity.board.dto.BoardResponse;
import com.develonity.board.entity.Board;
import com.develonity.user.entity.User;
import org.springframework.data.domain.Page;

public interface BoardService {

  Page<BoardResponse> getScrapBoardPage(User user,
      BoardPage boardPage);

  String getNicknameByBoard(Board Board);
}