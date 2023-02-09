package com.develonity.comment.controller;

import com.develonity.comment.service.CommentLikeService;
import com.develonity.common.security.users.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentLikeController {

  private final CommentLikeService commentLikeService;

  // 좋아요 추가
  @PostMapping("{commentId}/Likes")
  public ResponseEntity addLike(@PathVariable Long commentId, @AuthenticationPrincipal
  UserDetailsImpl userDetails) {
    Boolean like = commentLikeService.addLike(commentId, userDetails.getUser());

    // 좋아요가 이미 있으면 (true) 익셉션 출력, 없으면 (false) 좋아요! 실행
    if (!like) {
      throw new IllegalStateException("이미 좋아요를 누르셨습니다.");
    } else {
      return new ResponseEntity<>("좋아요!", HttpStatus.OK);
    }
  }

  // 좋아요 취소
  @DeleteMapping("{commentId}/Likes")
  public ResponseEntity deleteLike(@PathVariable Long commentId,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    Boolean like = commentLikeService.deleteLike(commentId, userDetails.getUser());

    if (!like) {
      throw new IllegalStateException("좋아요가 없는 사용자입니다.");
    } else {
      return new ResponseEntity<>("좋아요 취소 완료!", HttpStatus.OK);
    }
  }
}
