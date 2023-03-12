package com.develonity.board.service;

import com.develonity.board.dto.BoardPage;
import com.develonity.board.dto.BoardResponse;
import com.develonity.board.entity.Board;
import com.develonity.board.repository.BoardRepository;
import com.develonity.user.entity.User;
import com.develonity.user.service.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

  private final BoardRepository boardRepository;
  private final ScrapService scrapService;
  private final UserService userService;

  @Override
  public Page<BoardResponse> getScrapBoardPage(User user,
      BoardPage boardPage) {

//유저가 스크랩한 모든 게시글 아이디
    List<Long> scrapBoardIds = scrapService.getScrapBoardIds(user.getId());
    List<Board> scrapBoards = boardRepository.findAllByIdIn(scrapBoardIds);

    Page<Board> scrapBoardPage = new PageImpl<>(scrapBoards);
    return scrapBoardPage.map(
        board -> BoardResponse.toBoardResponse(board, getNicknameByBoard(board)));
  }


  @Override
  public String getNicknameByBoard(Board board) {
    return userService.getProfile(board.getUserId()).getNickname();
  }
}

