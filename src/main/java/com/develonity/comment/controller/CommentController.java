package com.develonity.comment.controller;

import com.develonity.comment.dto.CommentList;
import com.develonity.comment.dto.CommentRequest;
import com.develonity.comment.dto.CommentResponse;
import com.develonity.comment.service.CommentService;
import com.develonity.common.security.users.UserDetailsImpl;
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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class CommentController {

  private final CommentService commentService;

  // 댓글 전체 조회
  @GetMapping("/api/comments")
  public Page<CommentResponse> getAllComment(@AuthenticationPrincipal UserDetailsImpl userDetails,
      CommentList commentList) {
    return commentService.getAllComment(userDetails.getUser(), commentList);

  }

  // 내가 쓴 댓글 전체 조회
  @GetMapping("/api/user/{userid}/comments")
  public Page<CommentResponse> getMyComments(
      CommentList commentList,
      @PathVariable Long userid,
      @AuthenticationPrincipal UserDetailsImpl userDetails
  ) {
    return commentService.getMyComments(commentList, userid, userDetails.getUser());
  }

  // 질문게시글 답변 작성
  @PostMapping("/api/comments/question/{questionBoardId}")
  @ResponseStatus(HttpStatus.CREATED)
  public CommentResponse createQuestionComment(
      @PathVariable Long questionBoardId,
      @RequestBody
      CommentRequest requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
    return commentService.createQuestionComment(questionBoardId, requestDto, userDetails.getUser());
  }

  // 질문게시글 답변 수정
  @PutMapping("/api/comments/question/{questionBoardId}/{commentId}")
  public ResponseEntity<String> updateQuestionComment(
      @PathVariable Long questionBoardId,
      @PathVariable Long commentId, @RequestBody CommentRequest request,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    commentService.updateQuestionComment(questionBoardId, commentId, request,
        userDetails.getUser());
    return new ResponseEntity<>("답변 수정 완료!", HttpStatus.OK);
  }

  // 질문게시글 답변 삭제
  @DeleteMapping("/api/comments/question/{commentId}")
  public ResponseEntity<String> deleteQuestionComment(
      @PathVariable Long commentId,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    commentService.deleteQuestionComment(commentId, userDetails.getUser());
    return new ResponseEntity<>("답변 삭제 완료!", HttpStatus.OK);
  }

  // 잡담게시글 댓글 작성
  @PostMapping("/api/comments/community/{communityBoardId}")
  public ResponseEntity<String> createCommunityComment(
      @PathVariable Long communityBoardId,
      @RequestBody CommentRequest request, @AuthenticationPrincipal UserDetailsImpl userDetails) {
    commentService.createCommunityComment(communityBoardId, request, userDetails.getUser());
    return new ResponseEntity<>("잡담 댓글 작성 완료!", HttpStatus.CREATED);
  }

  // 잡담게시글 댓글 수정
  @PutMapping("/api/comments/community/{communityBoardId}/{commentId}")
  public ResponseEntity<String> updateCommunityComment(
      @PathVariable Long communityBoardId,
      @PathVariable Long commentId, @RequestBody CommentRequest request,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    commentService.updateCommunityComment(communityBoardId, commentId, request,
        userDetails.getUser());
    return new ResponseEntity<>("잡담 댓글 수정 완료!", HttpStatus.OK);
  }

  // 잡담게시글 댓글 삭제
  @DeleteMapping("/api/comments/community/{commentId}")
  public ResponseEntity<String> deleteCommunityComment(
      @PathVariable Long commentId,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    commentService.deleteCommunity(commentId, userDetails.getUser());
    return new ResponseEntity<>("잡담 댓글 삭제 완료!", HttpStatus.OK);
  }

}
