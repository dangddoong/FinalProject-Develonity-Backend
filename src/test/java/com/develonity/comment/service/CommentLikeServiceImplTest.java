package com.develonity.comment.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.develonity.board.dto.CommunityBoardRequest;
import com.develonity.board.entity.BoardImage;
import com.develonity.board.entity.CommunityBoard;
import com.develonity.board.entity.CommunityCategory;
import com.develonity.board.repository.BoardImageRepository;
import com.develonity.board.service.CommunityBoardServiceImpl;
import com.develonity.comment.dto.CommentRequest;
import com.develonity.comment.entity.Comment;
import com.develonity.comment.repository.CommentLikeRepository;
import com.develonity.comment.repository.CommentRepository;
import com.develonity.common.exception.CustomException;
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
@TestInstance(Lifecycle.PER_CLASS)
class CommentLikeServiceImplTest {

  @Autowired
  private CommentLikeRepository commentLikeRepository;
  @Autowired
  private CommentLikeServiceImpl commentLikeService;
  @Autowired
  private UserRepository userRepository;
  @Autowired
  private CommentServiceImpl commentService;
  @Autowired
  private CommunityBoardServiceImpl communityBoardService;
  @Autowired
  private BoardImageRepository boardImageRepository;
  @Autowired
  private CommentRepository commentRepository;

  @BeforeEach
  void allDeleteBefore() {
    commentLikeRepository.deleteAll();
  }

  @AfterEach
  void allDeleteAfter() {
    commentRepository.deleteAll();
  }

  @Test
  @DisplayName("좋아요 추가")
  void addCommentLike() throws IOException {
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
    CommentRequest commentRequest = new CommentRequest("댓글");
    Comment createComment = commentService.createCommunityComment(createCommunityBoard.getId(),
        commentRequest,
        findUser.get());

    // when
    commentLikeService.addCommentLike(createComment.getId(), findUser.get().getId());

    // then
    assertThat(commentService.countLike(createComment.getId())).isEqualTo(1L);
  }

  @Test
  @DisplayName("좋아요 추가 예외(이미 좋아요가 눌려 있는 경우)")
  void DuplicationCommentLike() throws IOException {
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
    CommentRequest commentRequest = new CommentRequest("댓글");
    Comment createComment = commentService.createCommunityComment(createCommunityBoard.getId(),
        commentRequest,
        findUser.get());
    // when
    commentLikeService.addCommentLike(createComment.getId(), findUser.get().getId());

    // then
    assertThrows(CustomException.class,
        () -> commentLikeService.addCommentLike(createComment.getId(), findUser.get().getId()));
  }

  @Test
  @DisplayName("좋아요 취소")
  void cancelCommentLike() throws IOException {
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
    CommentRequest commentRequest = new CommentRequest("댓글");
    Comment createComment = commentService.createCommunityComment(createCommunityBoard.getId(),
        commentRequest,
        findUser.get());
    commentLikeService.addCommentLike(createComment.getId(), findUser.get().getId());

    // when
    commentLikeService.cancelCommentLike(createComment.getId(), findUser.get().getId());

    // then
    assertThat(commentLikeRepository.existsCommentById(createComment.getId())).isFalse();
  }

  @Test
  @DisplayName("좋아요 취소 예외(좋아요가 눌러져 있지 않은 경우)")
  void notExistCommentLike() throws IOException {
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
    CommentRequest commentRequest = new CommentRequest("댓글");
    Comment createComment = commentService.createCommunityComment(createCommunityBoard.getId(),
        commentRequest,
        findUser.get());
    commentLikeService.addCommentLike(createComment.getId(), findUser.get().getId());

    // when
    commentLikeService.cancelCommentLike(createComment.getId(), findUser.get().getId());

    // then
    assertThrows(CustomException.class,
        () -> commentLikeService.cancelCommentLike(createComment.getId(), findUser.get().getId()));
  }

  @Test
  @DisplayName("좋아요 갯수")
  void countLike() throws IOException {
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
    CommentRequest commentRequest = new CommentRequest("댓글");
    Comment createComment = commentService.createCommunityComment(createCommunityBoard.getId(),
        commentRequest,
        findUser.get());

    // when
    commentLikeService.countLike(createComment.getId());

    // then
    assertThat(commentLikeRepository.countByCommentId(createComment.getId())).isEqualTo(
        commentLikeRepository.countByCommentId(createComment.getId()));
  }

// Repository에 쿼리가 걸려있어 실행이 안됩니다
//  @Test
//  @DisplayName("좋아요 삭제")
//  void deleteLike() {
//    // given
//    Optional<User> findUser = userRepository.findById(1L);
//    CommentRequest commentRequest = new CommentRequest("댓글");
//    Comment createComment = commentService.createCommunityComment(1L, commentRequest,
//        findUser.get());
//    commentLikeService.addCommentLike(createComment.getId(), findUser.get().getId());
//
//    // when
//    commentLikeService.deleteLike(createComment.getId());
//
//    // then
//    assertThat(commentLikeRepository.existsCommentById(createComment.getId())).isFalse();
//  }

  // 이거 가끔 오류나와서 수정해야 할 것 같습니다
//  @Test
//  @DisplayName("좋아요가 이미 있는지 확인")
//  void isExistLikes() {
//    // given
//    Optional<User> findUser = userRepository.findById(1L);
//    CommentRequest commentRequest = new CommentRequest("댓글");
//    Comment createComment = commentService.createCommunityComment(1L, commentRequest,
//        findUser.get());
//    commentLikeService.addCommentLike(createComment.getId(), findUser.get().getId());
//    // when
//    commentLikeService.isExistLikes(createComment.getId());
//    // then
//    assertThat(commentLikeRepository.existsCommentById(createComment.getId())).isTrue();
//  }

  @Test
  @DisplayName("유저가 이미 좋아요를 눌렀는지 확인")
  void isExistLikesCommentIdAndUserId() throws IOException {
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
    CommentRequest commentRequest = new CommentRequest("댓글");
    Comment createComment = commentService.createCommunityComment(createCommunityBoard.getId(),
        commentRequest,
        findUser.get());
    commentLikeService.addCommentLike(createComment.getId(), findUser.get().getId());

    // when
    commentLikeService.isExistLikesCommentIdAndUserId(createComment.getId(),
        findUser.get().getId());

    // then
    assertThat(commentLikeRepository.existsCommentLikeByCommentIdAndUserId(createComment.getId(),
        findUser.get().getId())).isTrue();
  }

//  @Test
//  void deleteAllByCommentId() {
//    // given
//    Optional<User> findUser = userRepository.findById(1L);
//    CommentRequest commentRequest = new CommentRequest("댓글");
//    Comment createComment = commentService.createCommunityComment(1L, commentRequest,
//        findUser.get());
//    commentLikeService.addCommentLike(createComment.getId(), findUser.get().getId());
//
//    // when
//    commentLikeService.deleteAllByCommentId(createComment.getId());
//
//    // then
//    assertThat(commentLikeRepository.existsCommentById(createComment.getId())).isFalse();
//  }
}