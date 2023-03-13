package com.develonity.comment.controller;

import com.develonity.board.entity.QuestionBoard;
import com.develonity.board.service.QuestionBoardService;
import com.develonity.comment.dto.CommentPageDto;
import com.develonity.comment.dto.CommentRequest;
import com.develonity.comment.dto.CommentResponse;
import com.develonity.comment.dto.CommentSearchCond;
import com.develonity.comment.entity.Comment;
import com.develonity.comment.service.CommentService;
import com.develonity.common.exception.CustomException;
import com.develonity.common.exception.ExceptionStatus;
import com.develonity.common.security.users.UserDetailsImpl;
import com.develonity.user.entity.User;
import com.develonity.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class CommentController {

  private final CommentService commentService;
  private final QuestionBoardService questionBoardService;

  private final UserService userService;

  //  Querydsl 댓글 전체 조회
  @GetMapping("/api/comments/all")
  public Page<CommentResponse> getAllComment(CommentPageDto commentPageDto,
      CommentSearchCond commentSearchCond) {
    return commentService.getAllComment(commentPageDto, commentSearchCond);

  }

  //게시글별 댓글 대댓글 전체 조회
  @GetMapping("/api/comments")
  public Page<CommentResponse> getScrapsPage(
      @AuthenticationPrincipal UserDetailsImpl userDetails,
      @RequestParam Long boardId
  ) {
    return commentService.getCommentsByBoard(boardId, userDetails.getUser());
  }


  // Querydsl 내가 쓴 댓글 전체 조회
  @GetMapping("/api/user/me/comments")
  public Page<CommentResponse> getMyComments(CommentPageDto commentPageDto,
      CommentSearchCond commentSearchCond, @AuthenticationPrincipal UserDetailsImpl userDetails) {
    return commentService.getMyComments(commentPageDto, commentSearchCond,
        userDetails.getUser().getId());
  }

  // 질문게시글 답변 작성
  @PostMapping("/api/question-comments")
  @ResponseStatus(HttpStatus.CREATED)
  public ResponseEntity<String> createQuestionComment(
      @RequestParam Long questionBoardId,
      @RequestBody
      CommentRequest requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
    if (questionBoardService.getQuestionBoardAndCheckSameUser(questionBoardId,
        userDetails.getUserId())) {
      throw new CustomException(ExceptionStatus.NOT_ALLOWED);
    }
    commentService.createQuestionComment(questionBoardId, requestDto, userDetails.getUser());
    return new ResponseEntity<>("답변 작성 완료!", HttpStatus.CREATED);
  }

  // 질문게시글 답변 수정
  @PutMapping("/api/question-comments/{commentId}")
  public ResponseEntity<String> updateQuestionComment(
      @PathVariable Long commentId, @RequestBody CommentRequest request,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    commentService.updateQuestionComment(commentId, request,
        userDetails.getUser());
    return new ResponseEntity<>("답변 수정 완료!", HttpStatus.OK);
  }

  // 질문게시글 답변 삭제
  @DeleteMapping("/api/question-comments/{commentId}")
  public ResponseEntity<String> deleteQuestionComment(
      @PathVariable Long commentId,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    commentService.deleteQuestionComment(commentId, userDetails.getUser());
    return new ResponseEntity<>("답변 삭제 완료!", HttpStatus.OK);
  }

  //질문게시글 답변 채택
  @PostMapping("/api/comments/{commentId}/adoption")
  public ResponseEntity<String> adoptComment(@PathVariable Long commentId,
      @RequestParam Long boardId,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {

    QuestionBoard questionBoard = questionBoardService.getQuestionBoardAndCheck(boardId);
    Comment comment = commentService.getComment(commentId);
    User commentUser = userService.getUserAndCheck(comment.getUserId());
    questionBoardService.checkUser(questionBoard, userDetails.getUser().getId());
    if (questionBoard.isAlreadyAdopted()) {
      throw new CustomException(ExceptionStatus.ALREADY_ADOPTED);
    }
    questionBoard.changeStatus();
    userService.addGiftPoint(questionBoard.getPrizePoint(), commentUser);
    userService.addRespectPoint(10, commentUser);
    commentService.adoptComment(comment);
    return new ResponseEntity<>("답변 채택 완료!", HttpStatus.OK);
  }

  // 잡담게시글 댓글 작성
  @PostMapping("/api/community-comments")
  public ResponseEntity<String> createCommunityComment(
      @RequestParam Long communityBoardId,
      @RequestBody CommentRequest request, @AuthenticationPrincipal UserDetailsImpl userDetails) {
    commentService.createCommunityComment(communityBoardId, request, userDetails.getUser());
    return new ResponseEntity<>("잡담 댓글 작성 완료!", HttpStatus.CREATED);
  }

  // 잡담게시글 댓글 수정
  @PutMapping("/api/community-comments/{commentId}")
  public ResponseEntity<String> updateCommunityComment(
      @PathVariable Long commentId, @RequestBody CommentRequest request,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    commentService.updateCommunityComment(commentId, request,
        userDetails.getUser());
    return new ResponseEntity<>("잡담 댓글 수정 완료!", HttpStatus.OK);
  }

  // 잡담게시글 댓글 삭제
  @DeleteMapping("/api/community-comments/{commentId}")
  public ResponseEntity<String> deleteCommunityComment(
      @PathVariable Long commentId,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    commentService.deleteCommunity(commentId, userDetails.getUser());
    return new ResponseEntity<>("잡담 댓글 삭제 완료!", HttpStatus.OK);
  }


}
