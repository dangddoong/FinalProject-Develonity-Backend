package com.develonity.comment.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.develonity.board.dto.CommunityBoardRequest;
import com.develonity.board.entity.BoardImage;
import com.develonity.board.entity.CommunityBoard;
import com.develonity.board.entity.CommunityCategory;
import com.develonity.board.repository.BoardImageRepository;
import com.develonity.board.service.CommunityBoardServiceImpl;
import com.develonity.comment.dto.CommentRequest;
import com.develonity.comment.dto.ReplyCommentRequest;
import com.develonity.comment.entity.Comment;
import com.develonity.comment.entity.ReplyComment;
import com.develonity.comment.repository.CommentRepository;
import com.develonity.comment.repository.ReplyCommentRepository;
import com.develonity.user.entity.User;
import com.develonity.user.repository.UserRepository;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

@SpringBootTest
//@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(Lifecycle.PER_CLASS)
class ReplyCommentServiceImplTest {

  @Autowired
  private CommentRepository commentRepository;
  @Autowired
  private ReplyCommentRepository replyCommentRepository;
  @Autowired
  private UserRepository userRepository;
  @Autowired
  private CommentService commentService;
  @Autowired
  private ReplyCommentServiceImpl replyCommentService;
  @Autowired
  private CommunityBoardServiceImpl communityBoardService;
  @Autowired
  private BoardImageRepository boardImageRepository;

  @BeforeEach
  void allDeleteBefore() {
    replyCommentRepository.deleteAll();
  }

  @AfterEach
  void allDeleteAfter() {
    commentRepository.deleteAll();
  }

  @Test
  @DisplayName("대댓글 작성")
  void createReplyComment() throws IOException {
    // given

    // 유저 id로 찾아서 저장
    Optional<User> findUser = userRepository.findById(1L);

    // 게시글 작성
    CommunityBoardRequest communityBoardRequest = new CommunityBoardRequest("제목", "내용",
        CommunityCategory.NORMAL);
    List<MultipartFile> multipartFiles = new ArrayList<>();
    MockMultipartFile multipartFile = new MockMultipartFile("files", "imageFile.jpeg", "image/jpeg",
        "<<jpeg data>>".getBytes());
    multipartFiles.add(multipartFile);

    CommunityBoard createCommunityBoard = communityBoardService.createCommunityBoard(
        communityBoardRequest,
        multipartFiles, findUser.get());

    List<BoardImage> originBoardImageList = boardImageRepository.findAllByBoardId(
        createCommunityBoard.getId());
    List<String> originImagePaths = new ArrayList<>();
    for (BoardImage boardImage : originBoardImageList) {
      originImagePaths.add(boardImage.getImagePath());
    }

    // 댓글 작성
    CommentRequest commentRequest = new CommentRequest("댓글 작성");
    Comment createComment = commentService.createCommunityComment(createCommunityBoard.getId(),
        commentRequest,
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
  void updateReplyComment() throws IOException {
    // given
    // 유저 id로 찾아서 저장
    Optional<User> findUser = userRepository.findById(1L);

    // 게시글 작상
    CommunityBoardRequest communityBoardRequest = new CommunityBoardRequest("제목", "내용",
        CommunityCategory.NORMAL);
    List<MultipartFile> multipartFiles = new ArrayList<>();
    MockMultipartFile multipartFile = new MockMultipartFile("files", "imageFile.jpeg", "image/jpeg",
        "<<jpeg data>>".getBytes());
    multipartFiles.add(multipartFile);

    CommunityBoard createCommunityBoard = communityBoardService.createCommunityBoard(
        communityBoardRequest,
        multipartFiles, findUser.get());

    List<BoardImage> originBoardImageList = boardImageRepository.findAllByBoardId(
        createCommunityBoard.getId());
    List<String> originImagePaths = new ArrayList<>();
    for (BoardImage boardImage : originBoardImageList) {
      originImagePaths.add(boardImage.getImagePath());
    }

    // 댓글 작성
    CommentRequest commentRequest = new CommentRequest("댓글 작성");
    Comment createComment = commentService.createCommunityComment(createCommunityBoard.getId(),
        commentRequest,
        findUser.get());

    // 대댓글 작성
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
  void deleteReplyComment() throws IOException {
    // given
    // 유저 id로 찾아서 저장
    Optional<User> findUser = userRepository.findById(1L);

    // 게시글 작성
    CommunityBoardRequest communityBoardRequest = new CommunityBoardRequest("제목", "내용",
        CommunityCategory.NORMAL);
    List<MultipartFile> multipartFiles = new ArrayList<>();
    MockMultipartFile multipartFile = new MockMultipartFile("files", "imageFile.jpeg", "image/jpeg",
        "<<jpeg data>>".getBytes());
    multipartFiles.add(multipartFile);

    CommunityBoard createCommunityBoard = communityBoardService.createCommunityBoard(
        communityBoardRequest,
        multipartFiles, findUser.get());

    List<BoardImage> originBoardImageList = boardImageRepository.findAllByBoardId(
        createCommunityBoard.getId());
    List<String> originImagePaths = new ArrayList<>();
    for (BoardImage boardImage : originBoardImageList) {
      originImagePaths.add(boardImage.getImagePath());
    }

    // 댓글 작성
    CommentRequest commentRequest = new CommentRequest("댓글 작성");
    Comment createComment = commentService.createCommunityComment(createCommunityBoard.getId(),
        commentRequest,
        findUser.get());

    // 대댓글 작성
    ReplyCommentRequest replyCommentRequest = new ReplyCommentRequest("대댓글");
    ReplyComment createReplyComment = replyCommentService.createReplyComment(createComment.getId(),
        replyCommentRequest, findUser.get());
    // when
    replyCommentService.deleteReplyComment(createReplyComment.getId(), findUser.get());
    // then
    assertThat(replyCommentRepository.existsById(createReplyComment.getId())).isFalse();
  }
}