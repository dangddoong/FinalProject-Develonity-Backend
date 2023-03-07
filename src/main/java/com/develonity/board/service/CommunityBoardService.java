package com.develonity.board.service;

import com.develonity.board.dto.BoardPage;
import com.develonity.board.dto.BoardSearchCond;
import com.develonity.board.dto.CommunityBoardRequest;
import com.develonity.board.dto.CommunityBoardResponse;
import com.develonity.board.dto.CommunityBoardSearchCond;
import com.develonity.board.dto.PageDto;
import com.develonity.board.entity.CommunityBoard;
import com.develonity.user.entity.User;
import java.io.IOException;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

public interface CommunityBoardService {

  CommunityBoard createCommunityBoard(CommunityBoardRequest request,
      List<MultipartFile> multipartFiles,
      User user) throws IOException;


  CommunityBoard updateCommunityBoard(Long boardId, List<MultipartFile> multipartFiles,
      CommunityBoardRequest request, User user) throws IOException;

  void deleteCommunityBoard(Long boardId, User user);

  Page<CommunityBoardResponse> getCommunityBoardPage(User user,
      BoardPage communityBoardPage);

  CommunityBoardResponse getCommunityBoard(Long boardId, User user);

  void checkUser(CommunityBoard communityBoard, Long userId);

  CommunityBoard getCommunityBoardAndCheck(Long boardId);

  long countLike(Long boardId);

  Boolean ExistsBoard(Long boardId);

  String getNickname(Long userId);

  String getNicknameByCommunityBoard(CommunityBoard communityBoard);

  void deleteBoardImages(Long boardId);

  void upload(List<MultipartFile> multipartFiles, CommunityBoard communityBoard) throws IOException;

  void uploadOne(MultipartFile multipartFile, CommunityBoard communityBoard) throws IOException;

  void upgradeGrade(Long userId, Long boardId);

  List<String> getImagePaths(CommunityBoard communityBoard);

  boolean isGradeBoard(Long boardId);

  //test
  Page<CommunityBoardResponse> getTestCommunityBoardPage(User user,
      BoardPage communityBoardPage);

  //댓글 + 대댓글 카운트
  long countAllComments(Long boardId);

  Page<CommunityBoardResponse> searchCommunityBoardByCond(CommunityBoardSearchCond cond,
      PageDto pageDto);

  Page<CommunityBoardResponse>searchMyCommunityBoardByCond(CommunityBoardSearchCond cond,
      PageDto pageDto, Long userId);
}