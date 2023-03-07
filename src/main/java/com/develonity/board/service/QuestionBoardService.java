package com.develonity.board.service;

import com.develonity.board.dto.PageDto;
import com.develonity.board.dto.QuestionBoardRequest;
import com.develonity.board.dto.QuestionBoardResponse;
import com.develonity.board.dto.QuestionBoardSearchCond;
import com.develonity.board.dto.QuestionBoardUpdateRequest;
import com.develonity.board.entity.QuestionBoard;
import com.develonity.user.entity.User;
import java.io.IOException;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

public interface QuestionBoardService {


  QuestionBoard createQuestionBoard(QuestionBoardRequest request,
      List<MultipartFile> multipartFiles, User user) throws IOException;

  QuestionBoard updateQuestionBoard(Long boardId, List<MultipartFile> multipartFiles,
      QuestionBoardUpdateRequest request,
      User user) throws IOException;


  void deleteQuestionBoard(Long boardId, User user);


  QuestionBoardResponse getQuestionBoard(Long boardId, User user);

  QuestionBoard getQuestionBoardAndCheck(Long boardId);

  void checkUser(QuestionBoard questionBoard, Long userId);

  long countLike(Long boardId);

  boolean isExistBoard(Long boardId);

  String getNickname(Long userId);

  String getNicknameByQuestionBoard(QuestionBoard questionBoard);

  boolean getQuestionBoardAndCheckSameUser(Long boardId, Long userId);

  void upload(List<MultipartFile> multipartFiles, QuestionBoard questionBoard) throws IOException;

  void deleteBoardImages(Long boardId);

  //댓글만 카운트
  long countComments(Long boardId);

  Page<QuestionBoardResponse> searchQuestionBoardByCond(
      QuestionBoardSearchCond questionBoardSearchCond,
      PageDto pageDto);

  Page<QuestionBoardResponse> searchMyQuestionBoardByCond(QuestionBoardSearchCond cond,
      PageDto pageDto, Long userId);

  List<QuestionBoardResponse> questionBoardOrderBy(QuestionBoardSearchCond cond);

//  Page<QuestionBoardResponse> getQuestionBoardPage(User user,
//      BoardPage questionBoardPage);
//
//  //test
//  Page<QuestionBoardResponse> getTestQuestionBoardPage(User user,
//      BoardPage questionBoardPage);
}
