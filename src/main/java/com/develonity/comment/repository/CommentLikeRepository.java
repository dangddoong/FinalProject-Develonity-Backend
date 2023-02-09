package com.develonity.comment.repository;

import com.develonity.comment.entity.Comment;
import com.develonity.comment.entity.CommentLike;
import com.develonity.user.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {

  Optional<CommentLike> findByUserAndComment(User user, Comment comment);

}
