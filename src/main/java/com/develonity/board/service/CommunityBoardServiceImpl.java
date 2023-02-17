package com.develonity.board.service;

import com.develonity.board.dto.BoardPage;
import com.develonity.board.dto.CommunityBoardRequest;
import com.develonity.board.dto.CommunityBoardResponse;
import com.develonity.board.entity.BoardImage;
import com.develonity.board.entity.CommunityBoard;
import com.develonity.board.repository.BoardImageRepository;
import com.develonity.board.repository.CommunityBoardRepository;
import com.develonity.comment.service.CommentService;
import com.develonity.common.exception.CustomException;
import com.develonity.common.exception.ExceptionStatus;
import com.develonity.user.entity.User;
import com.develonity.user.service.UserService;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CommunityBoardServiceImpl implements CommunityBoardService {

  private final CommunityBoardRepository communityBoardRepository;

  private final BoardLikeService boardLikeService;
  private final BoardImageRepository boardImageRepository;

  private final UserService userService;

  private final CommentService commentService;

//  private final AwsS3Service awsS3Service;

  //  잡담 게시글 생성
  @Override
  @Transactional
  public void createCommunityBoard(CommunityBoardRequest request, User user) {
    CommunityBoard communityBoard = CommunityBoard.builder()
        .userId(user.getId())
        .title(request.getTitle())
        .content(request.getContent())
        .category(request.getCategory())
        .build();

    communityBoardRepository.save(communityBoard);
  }

  //  잡담 게시글 생성(+이미지)
//  @Override
//  @Transactional
//  public void createCommunityBoard(CommunityBoardRequest request,
//      List<MultipartFile> multipartFiles,
//      User user) throws IOException {
//    CommunityBoard communityBoard = CommunityBoard.builder()
//        .userId(user.getId())
//        .title(request.getTitle())
//        .content(request.getContent())
//        .category(request.getCategory())
//        .communityCategory(request.getCommunityCategory())
//        .build();
//
//    upload(multipartFiles, communityBoard);
//    communityBoardRepository.save(communityBoard);
//  }

  //잡담 게시글 수정
  @Override
  @Transactional
  public void updateCommunityBoard(Long boardId, CommunityBoardRequest request, User user) {
    CommunityBoard communityBoard = getCommunityBoardAndCheck(boardId);
    checkUser(communityBoard, user.getId());
    communityBoard.updateBoard(request.getTitle(), request.getContent(), request.getCategory());
    communityBoardRepository.save(communityBoard);
  }

  //잡담 게시글 수정(+이미지)
//  @Override
//  @Transactional
//  public void updateCommunityBoard(Long boardId, List<MultipartFile> multipartFiles,
//      CommunityBoardRequest request, User user) throws IOException {
//    CommunityBoard communityBoard = getCommunityBoardAndCheck(boardId);
//    checkUser(communityBoard, user.getId());
//    for (MultipartFile multipartFile : multipartFiles) {
//      if (!multipartFile.isEmpty()) {
//        deleteBoardImages(boardId);
//        upload(multipartFiles, communityBoard);
//      } else {
//        upload(multipartFiles, communityBoard);
//      }
//    }
//    communityBoard.updateBoard(request.getTitle(), request.getContent(), request.getCategory());
//    communityBoardRepository.save(communityBoard);
//  }

  //잡담 게시글 삭제
  @Override
  @Transactional
  public void deleteCommunityBoard(Long boardId, User user) {
    CommunityBoard communityBoard = getCommunityBoardAndCheck(boardId);
    checkUser(communityBoard, user.getId());
    if (boardLikeService.isExistLikes(boardId)) {
      boardLikeService.deleteLike(boardId);
    }
//    deleteBoardImages(boardId);
//    commentService.deleteCommentByBoardId(boardId);
    communityBoardRepository.deleteById(boardId);


  }

  //잡담 게시글 전체 조회
  @Override
  @Transactional(readOnly = true)
  public Page<CommunityBoardResponse> getCommunityBoardPage(User user,
      BoardPage communityBoardPage) {

    Page<CommunityBoard> communityBoardPages = communityBoardRepository.findByTitleContainingOrContentContaining(
        communityBoardPage.getTitle(),
        communityBoardPage.getContent(),
        communityBoardPage.toPageable());

    return communityBoardPages.map(
        communityBoard -> CommunityBoardResponse.toCommunityBoardResponse(communityBoard,
            getNicknameByCommunityBoard(communityBoard), getImagePaths(communityBoard)));
  }

  //잡담 게시글 선택 조회
  @Override
  @Transactional(readOnly = true)
  public CommunityBoardResponse getCommunityBoard(Long boardId, User user) {
    CommunityBoard communityBoard = getCommunityBoardAndCheck(boardId);
    boolean isLike = boardLikeService.isLike(boardId, user.getId());
    Long boardUserId = communityBoard.getUserId();
    String nickname = getNickname(boardUserId);

    List<BoardImage> boardImageList = boardImageRepository.findAllByBoardId(boardId);
//    boardImageList.stream().map(boardImage -> boardImage.getImagePath())
//        .collect(Collectors.toList());
    List<String> imagePaths = new ArrayList<>();
    for (BoardImage boardImage : boardImageList) {
      imagePaths.add(boardImage.getImagePath());
    }
    return new CommunityBoardResponse(communityBoard, nickname, countLike(boardId), isLike,
        imagePaths);
  }

  //등급 변경
  @Override
  public void upgradeGrade(User user, Long boardId) {
    if (userService.isLackedRespectPoint(user)) {
      throw new CustomException(ExceptionStatus.POINTS_IS_LACKING);
    }
    userService.upgradeGrade(user);

  }


  @Override
  public void checkUser(CommunityBoard communityBoard, Long userId) {
    if (!communityBoard.isWriter(userId)) {
      throw new CustomException(ExceptionStatus.BOARD_USER_NOT_MATCH);
    }
  }

  @Override
  public CommunityBoard getCommunityBoardAndCheck(Long boardId) {
    return communityBoardRepository.findById(boardId)
        .orElseThrow(() -> new CustomException(ExceptionStatus.BOARD_IS_NOT_EXIST));
  }

  @Override
  public int countLike(Long boardId) {
    return boardLikeService.countLike(boardId);
  }

  @Override
  public boolean isExistBoard(Long boardId) {
    return communityBoardRepository.existsBoardById(boardId);
  }

  @Override
  public String getNickname(Long userId) {
    return userService.getProfile(userId).getNickname();
  }

  @Override
  public String getNicknameByCommunityBoard(CommunityBoard communityBoard) {
    return userService.getProfile(communityBoard.getUserId()).getNickname();
  }

  @Override
  public List<String> getImagePaths(CommunityBoard communityBoard) {
    List<BoardImage> boardImageList = boardImageRepository.findAllByBoardId(communityBoard.getId());
    List<String> imagePaths = new ArrayList<>();
    for (BoardImage boardImage : boardImageList) {
      imagePaths.add(boardImage.getImagePath());
    }
    return imagePaths;
  }
}

//  @Override
//  public void upload(List<MultipartFile> multipartFiles, CommunityBoard communityBoard)
//      throws IOException {
//
//    List<String> uploadImagePaths = new ArrayList<>();
//    String dir = "/board/communityImage";
//    int checkNumber = 0;
//    for (MultipartFile multipartFile : multipartFiles) {
//      if (!multipartFile.isEmpty()) {
//        checkNumber = 1;
//      }
//    }
//    if (checkNumber == 1) {
//      uploadImagePaths = awsS3Service.upload(multipartFiles, dir);
//    }
//
//    for (String imagePath : uploadImagePaths) {
//      BoardImage boardImage = new BoardImage(imagePath, communityBoard);
//      boardImageRepository.save(boardImage);
//    }
//  }
//
//  @Override
//  public void deleteBoardImages(Long boardId) {
//    List<BoardImage> boardImages = boardImageRepository.findAllByBoardId(boardId);
//
//    List<String> imagePaths = new ArrayList<>();
//
//    for (BoardImage boardImage : boardImages) {
//      imagePaths.add(boardImage.getImagePath());
//    }
//    for (String imagePath : imagePaths) {
//      awsS3Service.deleteFile(imagePath);
//    }
//    boardImageRepository.deleteBoardImageByBoardId(boardId);
//  }
//}
