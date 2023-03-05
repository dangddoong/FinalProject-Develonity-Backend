package com.develonity.comment.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.develonity.comment.dto.CommentRequest;
import com.develonity.comment.dto.ReplyCommentRequest;
import com.develonity.comment.entity.Comment;
import com.develonity.comment.entity.ReplyComment;
import com.develonity.comment.repository.CommentRepository;
import com.develonity.comment.repository.ReplyCommentRepository;
import com.develonity.user.entity.User;
import com.develonity.user.repository.UserRepository;
import com.develonity.user.service.UserService;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
//@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(Lifecycle.PER_CLASS)
class ReplyCommentServiceImplTest {

  @Autowired
  private CommentRepository commentRepository;
  @Autowired
  private ReplyCommentRepository replyCommentRepository;
  @Autowired
  private UserService userService;
  @Autowired
  private UserRepository userRepository;
  @Autowired
  private CommentService commentService;
  @Autowired
  private ReplyCommentServiceImpl replyCommentService;

//  @BeforeAll
//  public void BeforeAll() throws IOException {
//    Long boardId = 1L;
//    Long commentId = 1L;
//    CommentRequest commentRequest = new CommentRequest("댓글");
//    ReplyCommentRequest replyCommentRequest = new ReplyCommentRequest("대댓글");
//    Optional<User> findUser = userRepository.findById(1L);
//
//    commentService.createQuestionComment(boardId, commentRequest, findUser.get());
//    replyCommentService.createReplyComment(commentId, replyCommentRequest, findUser.get());
//  }

  @Test
  @DisplayName("대댓글 작성")
  void createReplyComment() {
    // given
    Optional<User> findUser = userRepository.findById(1L);
    CommentRequest commentRequest = new CommentRequest("댓글 작성");
    Comment createComment = commentService.createCommunityComment(1L, commentRequest,
        findUser.get());
    ReplyCommentRequest replyCommentRequest = new ReplyCommentRequest("대댓글");
    // when
    ReplyComment createReplyComment = replyCommentService.createReplyComment(createComment.getId(),
        replyCommentRequest, findUser.get());
    // then
    assertThat(createReplyComment.getContent()).isEqualTo(replyCommentRequest.getContent());
  }

  @Test
  @DisplayName("대댓글 수정")
  void updateReplyComment() {
    // given
    Optional<User> findUser = userRepository.findById(1L);
    CommentRequest commentRequest = new CommentRequest("댓글 작성");
    Comment createComment = commentService.createCommunityComment(1L, commentRequest,
        findUser.get());
    ReplyCommentRequest replyCommentRequest = new ReplyCommentRequest("대댓글");
    ReplyCommentRequest updateReplyCommentRequest = new ReplyCommentRequest("대댓글 수정");
    ReplyComment createReplyComment = replyCommentService.createReplyComment(createComment.getId(),
        replyCommentRequest, findUser.get());
    // when
    ReplyComment replyComment = replyCommentService.updateReplyComment(createComment.getId(),
        createReplyComment.getId(),
        updateReplyCommentRequest, findUser.get());
    // then
    assertThat(replyComment.getContent()).isEqualTo(updateReplyCommentRequest.getContent());
  }

  @Test
  @DisplayName("대댓글 삭제")
  void deleteReplyComment() {
    // given
    Optional<User> findUser = userRepository.findById(1L);
    CommentRequest commentRequest = new CommentRequest("댓글 작성");
    Comment createComment = commentService.createCommunityComment(1L, commentRequest,
        findUser.get());
    ReplyCommentRequest replyCommentRequest = new ReplyCommentRequest("대댓글");
    ReplyComment createReplyComment = replyCommentService.createReplyComment(createComment.getId(),
        replyCommentRequest, findUser.get());
    // when
    replyCommentService.deleteReplyComment(createReplyComment.getId(), findUser.get());
    // then
    assertThat(replyCommentRepository.existsById(createReplyComment.getId())).isFalse();
  }
}