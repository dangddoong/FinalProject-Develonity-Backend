package com.develonity.comment.service;

import com.develonity.comment.dto.CommentList;
import com.develonity.comment.dto.CommentRequest;
import com.develonity.comment.dto.CommentResponse;
import com.develonity.comment.dto.ReplyCommentResponse;
import com.develonity.comment.entity.Comment;
import com.develonity.comment.repository.CommentRepository;
import com.develonity.common.exception.CustomException;
import com.develonity.common.exception.ExceptionStatus;
import com.develonity.user.entity.User;
import com.develonity.user.service.UserService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

  private final CommentRepository commentRepository;

  private final CommentLikeService commentLikeService;

  private final ReplyCommentService replyCommentService;

  private final UserService userService;


  // 댓글이 있는지 확인하는 기능
  @Override
  public Comment getComment(Long commentId) {
    return commentRepository.findById(commentId).orElseThrow(
        () -> new CustomException(ExceptionStatus.COMMENT_IS_NOT_EXIST)
    );
  }

  // 작성자와 현재 유저가 같은지 확인하는 기능
  private void checkUser(User user, Comment comment) {
    if (!getNicknameByComment(comment).equals(user.getNickname())) {
      throw new CustomException(ExceptionStatus.COMMENT_USER_NOT_MATCH);
    }
  }

//  // 전체 댓글 조회
//  @Override
//  @Transactional(readOnly = true)
//  public Page<CommentResponse> getAllComment(User user, CommentList commentList) {
//    // 페이징 처리
//    Page<Comment> commentPages = commentRepository.findBy(commentList.toPageable());
//    return commentPages.map(
//        comment -> new CommentResponse(comment, getNicknameByComment(comment),
//            countLike(comment.getId())));
//
//  }

  // 내가 쓴 댓글 전체 조회 (페이징 처리)
  @Override
  @Transactional(readOnly = true)
  public Page<CommentResponse> getMyComments(CommentList commentList, Long userId, User user) {

    if (!userId.equals(user.getId())) {
      throw new CustomException(ExceptionStatus.COMMENT_USER_NOT_MATCH);
    }

    Page<Comment> myCommentList = commentRepository.findAllByUserId(commentList.toPageable(),
        user.getId());
    return myCommentList.map(
        comment -> new CommentResponse(comment, getNicknameByComment(comment),
            countLike(comment.getId())));
  }

  //게시글에 달린 댓글,대댓글 조회
  @Override
  @Transactional(readOnly = true)
  public Page<CommentResponse> getCommentsByBoard(Long boardId,
      User user) {

    List<Comment> comments = commentRepository.findAllByBoardId(boardId);

    List<CommentResponse> commentResponses = new ArrayList<>();

    List<Long> userIds = new ArrayList<>();
    for (Comment comment : comments) {
//댓글안에 있는 유저 아이디 리스트로 넣어서 해시맵<userId,userNickname> 리턴 받음
      userIds.add(comment.getUserId());
    }
    HashMap<Long, String> userIdAndNickname = userService.getUserIdAndNickname(userIds);

    for (Comment comment : comments) {

      List<ReplyCommentResponse> replyCommentResponses = replyCommentService.getReplyCommentResponses(
          comment);
      boolean hasLike = commentLikeService.isExistLikesCommentIdAndUserId(comment.getId(),
          user.getId());

      //댓글dto의 닉네임에 위의 해시맵 이용해서 해당 유저 닉네임 넣어주기
      CommentResponse response = new CommentResponse(comment,
          userIdAndNickname.get(comment.getUserId()),
          countLike(comment.getId()), hasLike, replyCommentResponses);
      commentResponses.add(response);
    }
    Collections.reverse(commentResponses);
    return new PageImpl<>(commentResponses);

  }

  // 질문게시글 답변 작성
  @Override
  @Transactional
  public void createQuestionComment(Long questionBoardId, CommentRequest requestDto,
      User user) {
//    if (existsCommentByBoardIdAndUserId(questionBoardId, user.getId())) {
//      throw new CustomException(ExceptionStatus.COMMENT_IS_EXIST);
//    }
    // 댓글 생성
    Comment comment = new Comment(user, requestDto, questionBoardId);
    commentRepository.save(comment);
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
    if (comment.isAdopted()) {
      throw new CustomException(ExceptionStatus.ADOPTED_QUESTION_BOARD);
    }
    // 권한 확인
    // 댓글 작성자와 수정하려는 유저 닉네임이 같지 않으면 익셉션 출력
    checkUser(user, comment);
    // 댓글 작성자이면 댓글 수정
    comment.update(request.getContent());
    commentRepository.save(comment);
  }

  // 질문 댓글 삭제 기능
  @Override
  @Transactional
  public void deleteQuestionComment(Long commentId, User user) {
    // 댓글이 있는지 확인
    Comment comment = getComment(commentId);
    // 권한 확인
    // 댓글 작성자와 수정하려는 유저 닉네임이 같지 않으면 익셉션 출력
    if (comment.isAdopted()) {
      throw new CustomException(ExceptionStatus.ADOPTED_QUESTION_BOARD);
    }

    if (commentLikeService.isExistLikes(commentId)) {
      commentLikeService.deleteAllByCommentId(commentId);
    }
    checkUser(user, comment);
    // 댓글 작성자면 댓글 삭제
    commentRepository.delete(comment);
  }

  //답변 채택 기능
  @Override
  @Transactional
  public void adoptComment(Comment comment) {
    comment.changeStatus();

  }


  // 잡담게시글 댓글 작성
  @Override
  @Transactional
  public void createCommunityComment(Long communityBoardId, CommentRequest request, User user) {
    // 게시글이 있는지 확인
//    Board board = boardService.getBoard(boardId);

    // 게시글이 있으면 댓글 작성
    Comment comment = new Comment(user, request, communityBoardId);
    commentRepository.save(comment);
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
  }

  // 잡담 댓글 삭제 기능
  @Override
  @Transactional
  public void deleteCommunity(Long commentId, User user) {

    // 댓글이 있는지 확인
    Comment comment = getComment(commentId);

    // 권한 확인
    // 댓글 작성자와 수정하려는 유저 닉네임이 같지 않으면 익셉션 출력
    checkUser(user, comment);
    // 댓글 작성자이면 댓글 삭제
    if (commentLikeService.isExistLikes(commentId)) {
      commentLikeService.deleteAllByCommentId(commentId);
    }
    commentRepository.delete(comment);
  }

  public void deleteCommentsByBoardId(Long boardId) {
//    if (existsCommentsByBoardId(boardId)) {
    List<Comment> comments = commentRepository.findAllByBoardId(boardId);
    List<Long> commentIdList = new ArrayList<>();
    for (Comment comment : comments) {
      commentIdList.add(comment.getId());
      replyCommentService.deleteAllReplyComments(comment);
    }
    for (Long commentId : commentIdList) {
      if (commentLikeService.isExistLikes(commentId)) {
        commentLikeService.deleteLike(commentId);
      }

      commentRepository.deleteAllByBoardId(boardId);
    }
  }

  @Override
  public boolean existsCommentByBoardIdAndUserId(Long boardId, Long userId) {
    return commentRepository.existsCommentByBoardIdAndUserId(boardId, userId);
  }

  @Override
  public boolean existsCommentsByBoardId(Long boardId) {
    return commentRepository.existsCommentsByBoardId(boardId);
  }

  // 닉네임을 가져오는 기능
  // userId를 매개변수로 받아 userService에 getProfile에서 userid에 닉네임을 가져온다.
  // ( 유저서비스에서 유저아이디에 프로필을 조회해서 닉네임을 가져오는 기능
  @Override
  public String getNickname(Long userId) {
    return userService.getProfile(userId).getNickname();
  }

  // 닉네임을 가져오는 기능
  // 유저서비스에서 댓글아이디에 프로필을 조회해서 닉네임을 가져오는 기능
  @Override
  public String getNicknameByComment(Comment comment) {
    return userService.getProfile(comment.getUserId()).getNickname();
  }

  //좋아요 갯수
  @Transactional
  @Override
  public long countLike(Long commentId) {
    return commentLikeService.countLike(commentId);
  }

  //게시글에 달린 댓글 (질문 게시글에 붙힐 것)
  @Override
  public long countComments(Long boardId) {
    return commentRepository.countByBoardId(boardId);
  }

  //게시글에 달린 댓글, 대댓글 갯수(잡담 게시글에 붙힐 것)
  @Override
  public long countCommentsAndReplyComments(Long boardId) {
    long countComments = commentRepository.countByBoardId(boardId);
    List<Comment> comments = commentRepository.findAllByBoardId(boardId);
    long countReplyComments = replyCommentService.countReplyComments(comments);

    return (countComments + countReplyComments);
  }

}




