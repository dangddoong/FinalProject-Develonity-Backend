package com.develonity.comment.repository;

import com.develonity.comment.entity.Comment;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

  Page<Comment> findAllByNickName(Pageable pageable, String username);

  Page<Comment> findBy(Pageable pageable);

  Optional<List<Comment>> findAllByBoardId(Long boardId);


}
