package com.develonity.comment.repository;

import com.develonity.comment.entity.Comment;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

  Page<Comment> findAllByUsername(Pageable pageable, String username);

  List<Comment> findByUsername(String username);

}
