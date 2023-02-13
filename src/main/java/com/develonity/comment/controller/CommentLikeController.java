package com.develonity.comment.controller;

import com.develonity.comment.service.CommentLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentLikeController {

  private final CommentLikeService commentLikeService;

  // 좋아요 추가
  @PostMapping("{commentId}/likes")
  public ResponseEntity addLike(@PathVariable Long commentId) {
    commentLikeService.addLike(commentId);
    return new ResponseEntity<>("좋아요!", HttpStatus.OK);
  }

  // 좋아요 취소
  @PostMapping("{commentId}/unlikes")
  public ResponseEntity cancelLike(@PathVariable Long commentId) {
    commentLikeService.cancelLike(commentId);
    return new ResponseEntity<>("좋아요 취소 완료!", HttpStatus.OK);
  }
}
