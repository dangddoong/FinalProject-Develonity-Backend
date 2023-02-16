package com.develonity.board.controller;

import com.develonity.board.dto.BoardPage;
import com.develonity.board.dto.CommunityBoardRequest;
import com.develonity.board.dto.CommunityBoardResponse;
import com.develonity.board.dto.QuestionBoardRequest;
import com.develonity.board.dto.QuestionBoardResponse;
import com.develonity.board.service.BoardLikeService;
import com.develonity.board.service.CommunityBoardService;
import com.develonity.board.service.QuestionBoardService;
import com.develonity.common.exception.CustomException;
import com.develonity.common.exception.ExceptionStatus;
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
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class BoardController {

  private final QuestionBoardService questionBoardService;

  private final CommunityBoardService communityBoardService;

//  private final AwsS3Service awsS3Service;

  private final BoardLikeService boardLikeService;

  //  질문게시글 생성
  @PostMapping("/question-boards")

  public ResponseEntity<String> createQuestionBoard(@RequestBody QuestionBoardRequest request,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    questionBoardService.createBoard(request, userDetails.getUser());
    return new ResponseEntity<>("질문 게시글이 생성되었습니다", HttpStatus.CREATED);
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

  //  잡담게시글 생성
  @PostMapping("/community-boards")

  public ResponseEntity<String> createCommunityBoard(@RequestBody CommunityBoardRequest request,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    communityBoardService.createCommunityBoard(request, userDetails.getUser());
    return new ResponseEntity<>("잡담 게시글이 생성되었습니다", HttpStatus.CREATED);
  }

  //질문게시글 수정
  @PatchMapping("/question-boards/{boardId}")
  public ResponseEntity<String> updateQuestionBoard(@PathVariable Long boardId,
      @RequestBody QuestionBoardRequest request,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    questionBoardService.updateBoard(boardId, request, userDetails.getUser());
    return new ResponseEntity<>("질문 게시글이 수정되었습니다.", HttpStatus.OK);
  }

  //잡담게시글 수정
  @PatchMapping("/community-boards/{boardId}")
  public ResponseEntity<String> updateCommunityBoard(@PathVariable Long boardId,
      @RequestBody CommunityBoardRequest request,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    communityBoardService.updateCommunityBoard(boardId, request, userDetails.getUser());
    return new ResponseEntity<>("잡담 게시글이 수정되었습니다.", HttpStatus.OK);
  }

  //질문게시글 삭제
  @DeleteMapping("/question-boards/{boardId}")
  public ResponseEntity<String> deleteQuestionBoard(@PathVariable Long boardId,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    questionBoardService.deleteBoard(boardId, userDetails.getUser());
    return new ResponseEntity<>("질문 게시글이 삭제되었습니다.", HttpStatus.OK);
  }

  //잡담게시글 삭제
  @DeleteMapping("/community-boards/{boardId}")
  public ResponseEntity<String> deleteCommunityBoard(@PathVariable Long boardId,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    communityBoardService.deleteCommunityBoard(boardId, userDetails.getUser());
    return new ResponseEntity<>("잡담 게시글이 삭제되었습니다.", HttpStatus.OK);
  }

  //질문게시글 선택 조회
  @GetMapping("/question-boards/{boardId}")
  public QuestionBoardResponse getQuestionBoard(@PathVariable Long boardId,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    return questionBoardService.getQuestionBoard(boardId, userDetails.getUser());
  }

  //잡담게시글 선택 조회
  @GetMapping("/community-boards/{boardId}")
  public CommunityBoardResponse getCommunityBoard(@PathVariable Long boardId,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    return communityBoardService.getCommunityBoard(boardId, userDetails.getUser());
  }

  //질문게시글 전체 조회
  @GetMapping("/question-boards")
  public Page<QuestionBoardResponse> getQuestionBoardsPage(
      @AuthenticationPrincipal UserDetailsImpl userDetails,
      BoardPage questionBoardPage
  ) {
    return questionBoardService.getQuetionBoardPage(userDetails.getUser(), questionBoardPage);
  }

  //잡담게시글 전체 조회
  @GetMapping("/community-boards")
  public Page<CommunityBoardResponse> getCommunityBoardsPage(
      @AuthenticationPrincipal UserDetailsImpl userDetails,
      BoardPage communityBoardPage
  ) {
    return communityBoardService.getCommunityBoardPage(userDetails.getUser(), communityBoardPage);
  }

  //질문 게시글 좋아요
  @PostMapping("/question-boards/{boardId}/likes")
  public ResponseEntity<String> addQuestionBoardLike(@PathVariable Long boardId,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {

    if (!questionBoardService.isExistBoard(boardId)) {
      throw new CustomException(ExceptionStatus.BOARD_IS_NOT_EXIST);
    }
    boardLikeService.addBoardLike(userDetails.getUser().getId(), boardId);
    return new ResponseEntity<>("좋아요 추가!", HttpStatus.CREATED);
  }


  //질문 게시글 좋아요 취소
  @DeleteMapping("/question-boards/{boardId}/unlikes")
  public ResponseEntity<String> cancelQuestionBoardLike(@PathVariable Long boardId,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    boardLikeService.cancelBoardLike(userDetails.getUser().getId(), boardId);
    return new ResponseEntity<>("좋아요 취소!", HttpStatus.OK);
  }

  //잡담 게시글 좋아요
  @PostMapping("/community-boards/{boardId}/likes")
  public ResponseEntity<String> addCommunityBoardLike(@PathVariable Long boardId,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    if (!communityBoardService.isExistBoard(boardId)) {
      throw new CustomException(ExceptionStatus.BOARD_IS_NOT_EXIST);
    }
    boardLikeService.addBoardLike(userDetails.getUser().getId(), boardId);
    return new ResponseEntity<>("좋아요 추가!", HttpStatus.CREATED);
  }

  //잡담 게시글 좋아요 취소
  @DeleteMapping("/community-boards/{boardId}/unlikes")
  public ResponseEntity<String> cancelCommunityBoardLike(@PathVariable Long boardId,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    boardLikeService.cancelBoardLike(userDetails.getUser().getId(), boardId);
    return new ResponseEntity<>("좋아요 취소!", HttpStatus.OK);
  }
}
