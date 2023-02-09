package com.develonity.comment.controller;

import com.develonity.comment.dto.CommentRequest;
import com.develonity.comment.dto.CommentResponse;
import com.develonity.comment.service.CommentService;
import com.develonity.common.security.users.UserDetailsImpl;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
  @GetMapping("/api/comments/{id}")
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
  public CommentResponse createQuestionComment(@RequestParam("question_board_id") Long boardId,
      @RequestBody
      CommentRequest requestDto, UserDetailsImpl userDetails) {
    return commentService.createQuestionComment(boardId, requestDto, userDetails.getUser());
  }

  // 질문게시글 답변 수정
  @PutMapping("/api/comments")
  public CommentResponse updateQuestionComment(@RequestParam("question_board_id") Long boardId,
      @RequestParam("question_comment_id") Long commentId, @RequestBody CommentRequest request,
      UserDetailsImpl userDetails) {

    return commentService.updateQuestionComment(boardId, commentId, request, userDetails.getUser());
  }

}
