package com.develonity.board.service;

import com.develonity.board.dto.BoardPage;
import com.develonity.board.dto.QuestionBoardRequest;
import com.develonity.board.dto.QuestionBoardResponse;
import com.develonity.board.entity.QuestionBoard;
import com.develonity.user.entity.User;
import java.io.IOException;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

public interface QuestionBoardService {


  void createQuestionBoard(QuestionBoardRequest request,
      List<MultipartFile> multipartFiles, User user) throws IOException;

  void updateQuestionBoard(Long boardId, List<MultipartFile> multipartFiles,
      QuestionBoardRequest request,
      User user) throws IOException;


  void deleteQuestionBoard(Long boardId, User user);

  Page<QuestionBoardResponse> getQuestionBoardPage(User user,
      BoardPage questionBoardPage);

  //test
  Page<QuestionBoardResponse> getTestQuestionBoardPage(User user,
      BoardPage questionBoardPage);

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
}
