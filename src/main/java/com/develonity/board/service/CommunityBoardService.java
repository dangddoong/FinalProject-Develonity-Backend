package com.develonity.board.service;

import com.develonity.board.dto.BoardPage;
import com.develonity.board.dto.CommunityBoardRequest;
import com.develonity.board.dto.CommunityBoardResponse;
import com.develonity.board.entity.CommunityBoard;
import com.develonity.user.entity.User;
import org.springframework.data.domain.Page;

public interface CommunityBoardService {

  void createCommunityBoard(CommunityBoardRequest request, User user);
//  void createCommunityBoard(CommunityBoardRequest request,
//      List<MultipartFile> multipartFiles,
//      User user) throws IOException;

  void updateCommunityBoard(Long boardId, CommunityBoardRequest request, User user);

//  void updateCommunityBoard(Long boardId, List<MultipartFile> multipartFiles,
//      CommunityBoardRequest request, User user) throws IOException;

  void deleteCommunityBoard(Long boardId, User user);

  Page<CommunityBoardResponse> getCommunityBoardPage(User user,
      BoardPage communityBoardPage);

  CommunityBoardResponse getCommunityBoard(Long boardId, User user);

  void checkUser(CommunityBoard communityBoard, Long userId);

  CommunityBoard getCommunityBoardAndCheck(Long boardId);

  int countLike(Long boardId);

  boolean isExistBoard(Long boardId);

//  void deleteBoardImages(Long boardId);
//
//  void upload(List<MultipartFile> multipartFiles, CommunityBoard communityBoard) throws IOException;

}
