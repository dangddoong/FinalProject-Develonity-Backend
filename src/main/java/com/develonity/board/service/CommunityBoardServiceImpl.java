package com.develonity.board.service;

import com.develonity.board.dto.BoardPage;
import com.develonity.board.dto.CommunityBoardRequest;
import com.develonity.board.dto.CommunityBoardResponse;
import com.develonity.board.entity.CommunityBoard;
import com.develonity.board.repository.CommunityBoardRepository;
import com.develonity.common.exception.CustomException;
import com.develonity.common.exception.ExceptionStatus;
import com.develonity.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CommunityBoardServiceImpl implements CommunityBoardService {

  private final CommunityBoardRepository communityBoardRepository;

  private final BoardLikeService boardLikeService;

  @Override
  public void createCommunityBoard(CommunityBoardRequest request, User user) {
    CommunityBoard communityBoard = CommunityBoard.builder()
        .userId(user.getId())
        .title(request.getTitle())
        .content(request.getContent())
        .category(request.getCategory())
        .build();

    communityBoardRepository.save(communityBoard);
  }

  @Override
  public void updateCommunityBoard(Long boardId, CommunityBoardRequest request, User user) {
    CommunityBoard communityBoard = getCommunityBoardAndCheck(boardId);
    checkUser(communityBoard, user.getId());
    communityBoard.updateBoard(request.getTitle(), request.getContent(), request.getCategory());
    communityBoardRepository.save(communityBoard);
  }

  @Override
  public void deleteCommunityBoard(Long boardId, User user) {
    CommunityBoard communityBoard = getCommunityBoardAndCheck(boardId);
    checkUser(communityBoard, user.getId());
    if (boardLikeService.isExistLikes(boardId)) {
      boardLikeService.deleteLike(boardId);
    }
    communityBoardRepository.deleteById(boardId);
  }

  @Override
  public Page<CommunityBoardResponse> getCommunityBoardPage(User user,
      BoardPage communityBoardPage) {

    Page<CommunityBoard> communityBoardPages = communityBoardRepository.findByTitleContainingOrContentContaining(
        communityBoardPage.getTitle(),
        communityBoardPage.getContent(),
        communityBoardPage.toPageable());

    return communityBoardPages.map(
        communityBoard -> CommunityBoardResponse.toCommunityBoardResponse(communityBoard, user));
  }

  @Override
  public CommunityBoardResponse getCommunityBoard(Long boardId, User user) {
    CommunityBoard communityBoard = getCommunityBoardAndCheck(boardId);
    /*islike메소드 트루인지 포스인지 확인하고*/
    boolean isLike = boardLikeService.isLike(boardId, user.getId());
    return new CommunityBoardResponse(communityBoard, user, countLike(boardId), isLike);
  }

  @Override
  public void checkUser(CommunityBoard communityBoard, Long userId) {
    if (!communityBoard.isWriter(userId)) {
      throw new CustomException(ExceptionStatus.BOARD_USER_NOT_MATCH);
    }
  }

  @Override
  public CommunityBoard getCommunityBoardAndCheck(Long boardId) {
    return communityBoardRepository.findById(boardId)
        .orElseThrow(() -> new CustomException(ExceptionStatus.BOARD_IS_NOT_EXIST));
  }

  @Override
  public int countLike(Long boardId) {
    return boardLikeService.countLike(boardId);
  }
}
