package com.develonity.board.controller;

import com.develonity.board.dto.BoardPage;
import com.develonity.board.dto.BoardResponse;
import com.develonity.board.dto.CommunityBoardRequest;
import com.develonity.board.dto.CommunityBoardResponse;
import com.develonity.board.dto.QuestionBoardRequest;
import com.develonity.board.dto.QuestionBoardResponse;
import com.develonity.board.entity.CommunityBoard;
import com.develonity.board.service.BoardLikeService;
import com.develonity.board.service.BoardService;
import com.develonity.board.service.CommunityBoardService;
import com.develonity.board.service.QuestionBoardService;
import com.develonity.board.service.ScrapService;
import com.develonity.common.exception.CustomException;
import com.develonity.common.exception.ExceptionStatus;
import com.develonity.common.security.users.UserDetailsImpl;
import java.io.IOException;
import java.util.List;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class BoardController {

  private final QuestionBoardService questionBoardService;

  private final CommunityBoardService communityBoardService;

  private final BoardService boardService;
  private final BoardLikeService boardLikeService;

  private final ScrapService scrapService;

  //  질문게시글 생성
  @PostMapping("/question-boards")
  @ResponseStatus(HttpStatus.CREATED)
  public ResponseEntity<String> createQuestionBoard(
      @RequestPart(required = false, name = "images") List<MultipartFile> multipartFiles,
      @RequestPart("request") QuestionBoardRequest request,
      @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {

    if (request.getQuestionCategory() == null) {
      throw new CustomException(ExceptionStatus.COMMENT_IS_NOT_EXIST);
    }

    questionBoardService.createQuestionBoard(request, multipartFiles, userDetails.getUser());
    return new ResponseEntity<>("질문 게시글이 생성되었습니다", HttpStatus.CREATED);
  }


  // 잡담 게시글 생성
  @PostMapping("/community-boards")
  @ResponseStatus(HttpStatus.CREATED)
  public ResponseEntity<String> createCommunityBoard(
      @RequestPart(required = false, name = "images") List<MultipartFile> multipartFiles,
      @RequestPart("request") CommunityBoardRequest request,
      @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {

    if (request.getCommunityCategory() == null) {
      throw new CustomException(ExceptionStatus.CATEGORY_IS_NOT_EXIST);
    }
    communityBoardService.createCommunityBoard(request, multipartFiles, userDetails.getUser());
    return new ResponseEntity<>("잡담 게시글이 생성되었습니다", HttpStatus.CREATED);
  }

  //질문게시글 수정
  @PatchMapping("/question-boards/{boardId}")
  public ResponseEntity<String> updateQuestionBoard(@PathVariable Long boardId,
      @RequestPart(required = false, name = "images") List<MultipartFile> multipartFiles,
      @RequestPart("request") QuestionBoardRequest request,
      @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {
    questionBoardService.updateQuestionBoard(boardId, multipartFiles, request,
        userDetails.getUser());
    return new ResponseEntity<>("질문 게시글이 수정되었습니다.", HttpStatus.OK);
  }

  //잡담 게시글 수정 (+이미지)
  @PatchMapping("/community-boards/{boardId}")
  public ResponseEntity<String> updateCommunityBoard(@PathVariable Long boardId,
      @RequestPart("images") List<MultipartFile> multipartFiles,
      @RequestPart("request") CommunityBoardRequest request,
      @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {
    communityBoardService.updateCommunityBoard(boardId, multipartFiles, request,
        userDetails.getUser());
    return new ResponseEntity<>("잡담 게시글이 수정되었습니다.", HttpStatus.OK);
  }

  //질문게시글 삭제
  @DeleteMapping("/question-boards/{boardId}")
  public ResponseEntity<String> deleteQuestionBoard(@PathVariable Long boardId,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    questionBoardService.deleteQuestionBoard(boardId, userDetails.getUser());
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
  @GetMapping("community-boards/{boardId}")
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


  //질문게시글 전체 조회(테스트용)
  @GetMapping("/question-boards/test")
  public Page<QuestionBoardResponse> getTestQuestionBoardsPage(
      @AuthenticationPrincipal UserDetailsImpl userDetails,
      BoardPage questionBoardPage
  ) {
    return questionBoardService.getTestQuetionBoardPage(userDetails.getUser(), questionBoardPage);
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
  @DeleteMapping("/question-boards/{boardId}/likes")
  public ResponseEntity<String> cancelQuestionBoardLike(@PathVariable Long boardId,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    boardLikeService.cancelBoardLike(userDetails.getUser().getId(), boardId);
    return new ResponseEntity<>("좋아요 취소!", HttpStatus.OK);
  }

  //잡담 게시글 좋아요
  @PostMapping("/community-boards/{boardId}/likes")
  public ResponseEntity<String> addCommunityBoardLike(@PathVariable Long boardId,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    if (!communityBoardService.ExistsBoard(boardId)) {
      throw new CustomException(ExceptionStatus.BOARD_IS_NOT_EXIST);
    }
    boardLikeService.addBoardLike(userDetails.getUser().getId(), boardId);
    return new ResponseEntity<>("좋아요 추가!", HttpStatus.CREATED);
  }

  //잡담 게시글 좋아요 취소
  @DeleteMapping("/community-boards/{boardId}/likes")
  public ResponseEntity<String> cancelCommunityBoardLike(@PathVariable Long boardId,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    boardLikeService.cancelBoardLike(userDetails.getUser().getId(), boardId);
    return new ResponseEntity<>("좋아요 취소!", HttpStatus.OK);
  }

  //등업 수락
  @PostMapping("/community-boards/{boardId}/grade")
  public ResponseEntity<String> changeGrade(@PathVariable Long boardId,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    if (!communityBoardService.ExistsBoard(boardId)) {
      throw new CustomException(ExceptionStatus.BOARD_IS_NOT_EXIST);
    }
    if (!communityBoardService.isGradeBoard(boardId)) {
      throw new CustomException(ExceptionStatus.CATEGORY_DO_NOT_MATCH);
    }
    CommunityBoard communityBoard = communityBoardService.getCommunityBoardAndCheck(boardId);
    communityBoardService.upgradeGrade(communityBoard.getUserId(), boardId);
    return new ResponseEntity<>("등급 변경 완료", HttpStatus.OK);
  }

  //스크랩 추가
  @PostMapping("/boards/{boardId}/scrap")
  public ResponseEntity<String> addScrap(@PathVariable Long boardId,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    scrapService.addScrap(userDetails.getUser().getId(), boardId);
    return new ResponseEntity<>("스크랩 추가!", HttpStatus.CREATED);
  }

  //스크랩 취소
  @DeleteMapping("/boards/{boardId}/scrap")
  public ResponseEntity<String> cancelScrap(@PathVariable Long boardId,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    scrapService.cancelScrap(userDetails.getUser().getId(), boardId);
    return new ResponseEntity<>("스크랩 취소!", HttpStatus.OK);
  }


  //유저 스크랩 게시물 전체 조회
  @GetMapping("/boards/scrap")
  public Page<BoardResponse> getScrapsPage(
      @AuthenticationPrincipal UserDetailsImpl userDetails,
      BoardPage boardPage
  ) {
    return boardService.getScrapBoardPage(userDetails.getUser(), boardPage);
  }


}
