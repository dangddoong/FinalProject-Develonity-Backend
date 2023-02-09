package com.develonity.comment.service;

import com.develonity.comment.dto.CommentRequest;
import com.develonity.comment.dto.CommentResponse;
import com.develonity.comment.entity.Comment;
import com.develonity.comment.repository.CommentRepository;
import com.develonity.user.entity.User;
import com.develonity.user.entity.UserRole;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

  private final CommentRepository commentRepository;

  // 댓글이 있는지 확인하는 기능
  private Comment getComment(Long commentId) {
    return commentRepository.findById(commentId).orElseThrow(
        () -> new IllegalArgumentException("댓글이 없습니다.")
    );
  }

  // 전체 댓글 조회
  @Override
  @Transactional(readOnly = true)
  public Page<CommentResponse> getAllComment(int page, int size) {
    // 페이징 처리
    Sort sort = Sort.by(Direction.DESC, "id");
    Pageable pageable = PageRequest.of(page, size, sort);

    Page<Comment> comments = commentRepository.findAll(pageable);
    Page<CommentResponse> commentResponseDto = comments.map(
        CommentResponse::toCommentResponseDto);
    return commentResponseDto;
  }

  // 내가 쓴 댓글 전체 조회
  @Override
  @Transactional(readOnly = true)
  public List<CommentResponse> getMyComments(int page, int size, Long userId, User user) {

    if (userId != user.getId()) {
      throw new IllegalStateException("권한이 없습니다");
    }
    Sort sort = Sort.by(Direction.DESC, "id");
    Pageable pageable = PageRequest.of(page, size, sort);

    Page<Comment> myCommentList = commentRepository.findAllByUsername(pageable, user.getNickName());
    List<CommentResponse> myCommentListDto = myCommentList.getContent()
        .stream()
        .map(CommentResponse::new)
        .collect(Collectors.toList());

    return myCommentListDto;
  }

  // 질문게시글 답변 작성
  @Override
  @Transactional
  public CommentResponse createQuestionComment(Long boardId, CommentRequest requestDto,
      User user) {
    // 게시물이 있는지 확인
//    Board board = boardService.getBoard(boardId);
    // 댓글 생성
    Comment comment = new Comment(user, requestDto);
    commentRepository.save(comment);
    return new CommentResponse(comment);
  }

  @Override
  public CommentResponse updateQuestionComment(Long boardId, Long commentId, CommentRequest request,
      User user) {
    // 게시물이 있는지 확인
//    Board board = boardService.getBoard(boardId);
    // 댓글이 있는지 확인
    Comment comment = getComment(commentId);

    // 권한 확인
    // 댓글 작성자와 유저 닉네임이 같지 않고, 어드민이 아닌 경우 익셉션 출력
    if (comment.getUsername() != user.getNickName() && user.getUserRole() != UserRole.ADMIN) {
      throw new IllegalStateException("댓글 작성자가 아닙니다.");
    }

    // 댓글 작성자이거나 어드민인 경우 댓글 수정
    commentRepository.save(comment);
    return new CommentResponse(comment);
  }
}
