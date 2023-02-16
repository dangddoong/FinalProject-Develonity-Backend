package com.develonity.comment.controller;

import com.develonity.comment.dto.ReplyCommentRequest;
import com.develonity.comment.service.ReplyCommentService;
import com.develonity.common.security.users.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ReplyCommentController {

  private final ReplyCommentService replyCommentService;

  // 대댓글 작성
  @PostMapping("/api/comments/{commentId}")
  public ResponseEntity<String> createReplyComment(@PathVariable Long commentId, @RequestBody
  ReplyCommentRequest request, @AuthenticationPrincipal UserDetailsImpl userDetails) {
    replyCommentService.createReplyComment(commentId, request, userDetails.getUser());
    return new ResponseEntity<>("대댓글 작성 완료!", HttpStatus.CREATED);
  }

  // 대댓글 수정
  @PutMapping("/api/comments/{commentId}/{replyCommentId}")
  public ResponseEntity<String> updateReplyComment(@PathVariable Long commentId,
      @PathVariable Long replyCommentId, @RequestBody ReplyCommentRequest request,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    replyCommentService.updateReplyComment(commentId, replyCommentId, request,
        userDetails.getUser());
    return new ResponseEntity<>("대댓글 수정 완료!", HttpStatus.OK);
  }

  // 대댓글 삭제
  @DeleteMapping("/api/comments/{replyCommentId}")
  public ResponseEntity<String> deleteReplyComment(@PathVariable Long replyCommentId,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    replyCommentService.deleteReplyComment(replyCommentId, userDetails.getUser());
    return new ResponseEntity<>("대댓글 삭제 완료!", HttpStatus.OK);
  }

}
