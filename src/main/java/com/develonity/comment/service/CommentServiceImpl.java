package com.develonity.comment.service;

import com.develonity.comment.dto.CommentList;
import com.develonity.comment.dto.CommentRequest;
import com.develonity.comment.dto.CommentResponse;
import com.develonity.comment.entity.Comment;
import com.develonity.comment.repository.CommentRepository;
import com.develonity.common.exception.CustomException;
import com.develonity.common.exception.ExceptionStatus;
import com.develonity.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

  private final CommentRepository commentRepository;

  private final CommentLikeService commentLikeService;


  // 댓글이 있는지 확인하는 기능
  private Comment getComment(Long commentId) {
    return commentRepository.findById(commentId).orElseThrow(
        () -> new CustomException(ExceptionStatus.COMMENT_IS_NOT_EXIST)
    );
  }

  // 작성자와 현재 유저가 같은지 확인하는 기능
  private void checkUser(User user, Comment comment) {
    if (comment.getNickName() != user.getNickName()) {
      throw new CustomException(ExceptionStatus.COMMENT_USER_NOT_MATCH);
    }
  }

  // 전체 댓글 조회
  @Override
  @Transactional(readOnly = true)
  public Page<CommentResponse> getAllComment(User user, CommentList commentList) {
    // 페이징 처리
    Page<Comment> commentPages = commentRepository.findBy(commentList.toPageable());
    return commentPages.map(
        comment -> new CommentResponse(comment, commentLikeService.addLike((comment.getId()))));
  }

  // 내가 쓴 댓글 전체 조회 (페이징 처리)
  @Override
  @Transactional(readOnly = true)
  public Page<CommentResponse> getMyComments(CommentList commentList, Long userId, User user) {

    if (userId != user.getId()) {
      throw new CustomException(ExceptionStatus.COMMENT_USER_NOT_MATCH);
    }

    Page<Comment> myCommentList = commentRepository.findAllByNickName(commentList.toPageable(),
        user.getNickName());
    return myCommentList.map(
        comment1 -> new CommentResponse(comment1, commentLikeService.addLike(comment1.getId())));
  }

  // 질문게시글 답변 작성
  @Override
  @Transactional
  public void createQuestionComment(Long questionBoardId, CommentRequest requestDto,
      User user) {
    // 게시물이 있는지 확인
//    Board board = boardRepository.findById(boardid).orElseThrow(
//        () -> new IllegalArgumentException("게시물이 없습니다.")
//    );

    // 댓글 생성
    Comment comment = new Comment(user, requestDto);
    commentRepository.save(comment);
    new CommentResponse(comment, commentLikeService.addLike(comment.getId()));
  }

  // 질문 게시글 수정
  @Override
  @Transactional
  public void updateQuestionComment(Long questionBoardId, Long commentId,
      CommentRequest request,
      User user) {
    // 게시물이 있는지 확인
//    Board board = boardService.getBoard(boardId);
    // 댓글이 있는지 확인
    Comment comment = getComment(commentId);

    // 권한 확인
    // 댓글 작성자와 수정하려는 유저 닉네임이 같지 않으면 익셉션 출력
    checkUser(user, comment);
    // 댓글 작성자이면 댓글 수정
    comment.update(request.getContent());
    commentRepository.save(comment);
    new CommentResponse(comment, commentLikeService.addLike(comment.getId()));

  }

  // 질문 댓글 삭제 기능
  @Override
  @Transactional
  public void deleteQuestionComment(Long commentId, User user) {
    // 댓글이 있는지 확인
    Comment comment = getComment(commentId);
    // 권한 확인
    // 댓글 작성자와 수정하려는 유저 닉네임이 같지 않으면 익셉션 출력
    checkUser(user, comment);

    if (commentLikeService.isExistLikes(commentId)) {
      commentLikeService.cancelLike(commentId);
    }
    // 댓글 작성자면 댓글 삭제
    commentRepository.delete(comment);


  }

  // 잡담게시글 댓글 작성
  @Override
  @Transactional
  public void createCommunityComment(Long communityBoardId, CommentRequest request, User user) {
    // 게시글이 있는지 확인
//    Board board = boardService.getBoard(boardId);

    // 게시글이 있으면 댓글 작성
    Comment comment = new Comment(user, request);
    commentRepository.save(comment);
    new CommentResponse(comment, commentLikeService.addLike(comment.getId()));
  }

  // 잡담 댓글 수정
  @Override
  @Transactional
  public void updateCommunityComment(Long communityBoardId, Long commentId, CommentRequest request,
      User user) {
    // 게시글이 있는지 확인
    // Board board = boardService.getBoard(boardId);

    // 댓글이 있는지 확인
    Comment comment = getComment(commentId);

    // 권한 확인
    // 댓글 작성자와 수정하려는 유저 닉네임이 같지 않거나, 어드민이 아니면 익셉션 출력
    checkUser(user, comment);
    // 댓글 작성자인 경우 댓글 수정
    comment.update(request.getContent());
    commentRepository.save(comment);
    new CommentResponse(comment, commentLikeService.addLike(comment.getId()));

  }

  @Override
  @Transactional
  public void deleteCommunity(Long commentId, User user) {

    // 댓글이 있는지 확인
    Comment comment = getComment(commentId);

    // 권한 확인
    // 댓글 작성자와 수정하려는 유저 닉네임이 같지 않으면 익셉션 출력
    checkUser(user, comment);
    // 댓글 작성자이면 댓글 삭제
    commentRepository.delete(comment);
  }
}
