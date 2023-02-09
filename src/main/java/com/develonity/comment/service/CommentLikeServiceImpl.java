package com.develonity.comment.service;

import com.develonity.comment.entity.Comment;
import com.develonity.comment.entity.CommentLike;
import com.develonity.comment.repository.CommentLikeRepository;
import com.develonity.comment.repository.CommentRepository;
import com.develonity.user.entity.User;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentLikeServiceImpl implements CommentLikeService {

  private final CommentLikeRepository commentLikeRepository;
  private final CommentRepository commentRepository;

  @Override
  @Transactional
  public Boolean addLike(Long commentId, User user) {

    // 댓글이 있는지 확인
    Comment comment = commentRepository.findById(commentId).orElseThrow(
        () -> new IllegalArgumentException("댓글이 없습니다.")
    );

    // 좋아요 상태 체크
    Optional<CommentLike> likeStatus = commentLikeRepository.findByUserAndComment(user, comment);

    // 좋아요를 하지 않았다면 addLike를 사용해 좋아요 갯수 추가
    if (likeStatus.isEmpty()) {
      commentLikeRepository.save(new CommentLike(user, comment));
      comment.addLike();
      return true;
    }
    // 이미 좋아요를 한 사용자일 경우
    return false;

  }

  @Override
  @Transactional
  public Boolean deleteLike(Long commentId, User user) {

    // 댓글이 있는지 확인
    Comment comment = commentRepository.findById(commentId).orElseThrow(
        () -> new IllegalArgumentException("댓글이 없습니다.")
    );

    Optional<CommentLike> likeStatus = commentLikeRepository.findByUserAndComment(user, comment);

    // 좋아요가 있을경우 deleteLike를 사용해 좋아요 취소
    if (likeStatus.isPresent()) {
      commentLikeRepository.deleteById(likeStatus.get().getId());
      comment.deleteLike();
      return true;
    }

    // 이미 좋아요 취소를 누른 사용자 일 경우
    return false;
  }
}
