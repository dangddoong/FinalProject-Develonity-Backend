package com.develonity.comment.service;

import com.develonity.comment.entity.CommentLike;
import com.develonity.comment.repository.CommentLikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentLikeServiceImpl implements CommentLikeService {

  private final CommentLikeRepository commentLikeRepository;

  @Override
  @Transactional
  public int addLike(Long commentId) {

    // 좋아요를 하지 않았다면 addLike를 사용해 좋아요 갯수 추가
    return commentLikeRepository.countByCommentId(commentId);

    // 이미 좋아요를 누른 사용자 일 경우
//    return false;
  }

  @Override
  @Transactional
  public void cancelLike(Long commentId) {
    commentLikeRepository.deleteById(commentId);
  }

  @Override
  @Transactional
  public void changeCommentLike(Long commentId, Long userId) {
    if (commentLikeRepository.existsCommentLikeByCommentIdAndUserId(commentId, userId)) {
      CommentLike commentLike = commentLikeRepository.findByCommentIdAndUserId(commentId, userId);
      commentLikeRepository.delete(commentLike);
    } else {
      CommentLike commentLike = new CommentLike(userId, commentId);
      commentLikeRepository.save(commentLike);
    }
  }

  @Override
  public boolean isExistLikes(Long commentId) {
    return commentLikeRepository.existsCommentById(commentId);
  }
}
