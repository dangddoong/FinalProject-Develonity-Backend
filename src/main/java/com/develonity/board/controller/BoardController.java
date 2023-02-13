package com.develonity.board.controller;

import com.develonity.board.dto.QuestionBoardPage;
import com.develonity.board.dto.QuestionBoardRequest;
import com.develonity.board.dto.QuestionBoardResponse;
import com.develonity.board.service.BoardLikeService;
import com.develonity.board.service.BoardService;
import com.develonity.common.security.users.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class BoardController {

  private final BoardService boardService;

//  private final AwsS3Service awsS3Service;

  private final BoardLikeService boardLikeService;

  //  질문게시글 생성
  @PostMapping("/question-boards")
  @ResponseStatus(HttpStatus.CREATED)
  public QuestionBoardResponse createQuestionBoard(@RequestBody QuestionBoardRequest request,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    return boardService.createBoard(request, userDetails.getUser());
  }

//  질문게시글 생성(+이미지)
//  @PostMapping("/question-boards")
//  @ResponseStatus(HttpStatus.CREATED)
//  public QuestionBoardResponse createQuestionBoard(
//      @RequestPart("images") List<MultipartFile> multipartFiles,
//      @RequestPart("request") QuestionBoardRequest request,
//      @AuthenticationPrincipal UserDetailsImpl userDetails)
//      throws IllegalAccessException, IOException {
//
//    if (multipartFiles == null) {
//      throw new IllegalAccessException();
//    }
//
//    return boardService.createBoard(request, multipartFiles, userDetails.getUser());
//
//  }

  //질문게시글 수정
  @PatchMapping("/question-boards/{boardId}")
  public ResponseEntity<String> updateQuestionBoard(@PathVariable Long boardId,
      @RequestBody QuestionBoardRequest request,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    boardService.updateBoard(boardId, request, userDetails.getUser());
    return new ResponseEntity<>("질문 게시글이 수정되었습니다.", HttpStatus.OK);
  }

  //질문게시글 삭제
  @DeleteMapping("/question-boards/{boardId}")
  public ResponseEntity<String> deleteQuestionBoard(@PathVariable Long boardId,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    boardService.deleteBoard(boardId, userDetails.getUser());
    return new ResponseEntity<>("질문 게시글이 삭제되었습니다.", HttpStatus.OK);
  }

  //  //질문게시글 선택 조회
  @GetMapping("/question-boards/{boardId}")
  public QuestionBoardResponse getQuestionBoard(@PathVariable Long boardId,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    return boardService.getQuestionBoard(boardId, userDetails.getUser());
  }

  //질문게시글 전체 조회
  @GetMapping("/question-boards")
  public Page<QuestionBoardResponse> getQuestionBoardsPage(
      @AuthenticationPrincipal UserDetailsImpl userDetails,
      QuestionBoardPage questionBoardPage
  ) {
    return boardService.getQuetionBoardPage(userDetails.getUser(), questionBoardPage);
  }

  //좋아요 , 좋아요 취소
  @PostMapping("/question-boards/{boardId}/likes")
  @ResponseStatus(HttpStatus.OK)
  public void changeBoardLike(@PathVariable Long boardId,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    boardLikeService.changeBoardLike(userDetails.getUser().getId(), boardId);
  }
}
