package com.develonity.comment.controller;

import com.develonity.comment.dto.CommentRequest;
import com.develonity.comment.dto.CommentResponse;
import com.develonity.comment.service.CommentService;
import com.develonity.common.security.users.UserDetailsImpl;
import java.util.List;
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
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class CommentController {

  private final CommentService commentService;

  // 댓글 전체 조회
  @GetMapping("/api/comments")
  public Page<CommentResponse> getAllComment(
      @RequestParam("page") int page,
      @RequestParam("size") int size
  ) {
    return commentService.getAllComment(page, size);
  }

  // 내가 쓴 댓글 전체 조회
  @GetMapping("/api/user/comments")
  public List<CommentResponse> getMyComments(
      @RequestParam("page") int page,
      @RequestParam("size") int size,
      @PathVariable Long userid,
      @AuthenticationPrincipal UserDetailsImpl userDetails
  ) {
    return commentService.getMyComments(page, size, userid, userDetails.getUser());
  }

  // 질문게시글 답변 작성
  @PostMapping("/api/comments")
  public ResponseEntity<String> createQuestionComment(
      @RequestParam("question-board-id") Long boardId,
      @RequestBody
      CommentRequest requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
    commentService.createQuestionComment(boardId, requestDto, userDetails.getUser());
    return new ResponseEntity<>("답변 작성 완료!", HttpStatus.CREATED);
  }

  // 질문게시글 답변 수정
  @PutMapping("/api/comments")
  public ResponseEntity<String> updateQuestionComment(
      @RequestParam("question-board-id") Long boardId,
      @RequestParam("question-comment-id") Long commentId, @RequestBody CommentRequest request,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    commentService.updateQuestionComment(boardId, commentId, request, userDetails.getUser());
    return new ResponseEntity<>("답변 수정 완료!", HttpStatus.OK);
  }

  // 질문게시글 답변 삭제
  @DeleteMapping("/api/omments")
  public ResponseEntity<String> deleteQuestionComment(
      @RequestParam("question-board-id") Long boardId,
      @RequestParam("question-comment-id") Long commentId,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    commentService.deleteQuestionComment(boardId, commentId, userDetails.getUser());
    return new ResponseEntity<>("답변 삭제 완료!", HttpStatus.OK);
  }

  // 잡담게시글 댓글 작성
  @PostMapping("/api/comments")
  public ResponseEntity<String> createCommunityComment(
      @RequestParam("community-board-id") Long boardId,
      @RequestBody CommentRequest request, @AuthenticationPrincipal UserDetailsImpl userDetails) {
    commentService.createCommunityComment(boardId, request, userDetails.getUser());
    return new ResponseEntity<>("잡담 댓글 작성 완료!", HttpStatus.CREATED);
  }

  // 잡담게시글 댓글 수정
  @PutMapping("/api/comments")
  public ResponseEntity<String> updateCommunityComment(
      @RequestParam("community-board-id") Long boardId,
      @RequestParam("community-comments-id") Long commentId, @RequestBody CommentRequest request,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    commentService.updateCommunityComment(boardId, commentId, request, userDetails.getUser());
    return new ResponseEntity<>("잡담 댓글 수정 완료!", HttpStatus.OK);
  }

  // 잡담게시글 댓글 삭제
  @DeleteMapping("/api/comments")
  public ResponseEntity<String> deleteCommunityComment(
      @RequestParam("community-board-id") Long boardId,
      @RequestParam("community-comment-id") Long commentId,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    commentService.deleteCommunity(boardId, commentId, userDetails.getUser());
    return new ResponseEntity<>("잡담 댓글 삭제 완료!", HttpStatus.OK);
  }

}
