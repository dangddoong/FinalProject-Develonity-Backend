//package com.develonity.board.service;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.mockito.ArgumentMatchers.anyLong;
//
//import com.develonity.board.dto.CommunityBoardRequest;
//import com.develonity.board.dto.CommunityBoardResponse;
//import com.develonity.board.entity.Category;
//import com.develonity.board.entity.CommunityBoard;
//import com.develonity.board.entity.SubCategory.CommunityCategory;
//import com.develonity.board.repository.CommunityBoardRepository;
//import com.develonity.user.entity.User;
//import com.develonity.user.repository.UserRepository;
//import com.develonity.user.service.UserService;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.web.multipart.MultipartFile;
//
//@SpringBootTest
//class CommunityBoardServiceImplTest {
//
//  @Autowired
//  private CommunityBoardRepository communityBoardRepository;
//
//  @Autowired
//  private BoardLikeService boardLikeService;
//
//  @Autowired
//  private UserService userService;
//
//  @Autowired
//  private CommunityBoardServiceImpl communityBoardService;
//
//  @Autowired
//  private UserRepository userRepository;
//
//  @Test
//  @DisplayName("잡담게시글 생성(빈이미지파일)")
//  void createCommunityBoard() throws IOException {
//
//    //given
//    CommunityBoardRequest request = new CommunityBoardRequest("제목1", "내용1",
//        Category.COMMUNITY_BOARD, 100, "aa.jpg",
//        CommunityCategory.NORMAL
//    );
//    User user1 = new User("user1", "user1", "realUser", "userNickname", "aaa", "aaa@aaa", "010",
//        "address", "zip");
//
//    User saveduser = userRepository.save(user1);
//    List<MultipartFile> multipartFiles = new ArrayList<MultipartFile>();
//
////    File file = new File("C:\\temp\\test.jpg");
////    FileItem fileItem = new DiskFileItem("file", Files.probeContentType(file.toPath()), false, file.getName(), (int) file.length(), file.getParentFile());
////
////    InputStream inputStream = new FileInputStream(file);
////    OutputStream outputStream = fileItem.getOutputStream();
////    IOUtils.copy(inputStream, outputStream);
////// or IOUtils.copy(new FileInputStream(file), fileItem.getOutputStream());
////
////    MultipartFile multipartFile = new CommonsMultipartFile(fileItem);
//
////    MultipartFile multipartFile = new MockMultipartFile("png",
////        new FileInputStream(new File(
////            "C:.png")));
////    multipartFiles.add(multipartFile);
//
//    CommunityBoard communityBoard1 = CommunityBoard.builder()
//        .userId(saveduser.getId())
//        .title(request.getTitle())
//        .content(request.getContent())
//        .category(request.getCategory())
//        .communityCategory(request.getCommunityCategory())
//        .build();
//
//    communityBoardRepository.save(communityBoard1);
//    //when
//    communityBoardService.createCommunityBoard(request, multipartFiles, saveduser);
//    CommunityBoardResponse communityBoardResponse1 = communityBoardService.getCommunityBoard(
//        communityBoard1.getId(), saveduser);
//
//    //then
//    assertThat(communityBoardResponse1.getTitle()).isEqualTo(communityBoard1.getTitle());
//    assertThat(communityBoardResponse1.getContent()).isEqualTo(communityBoard1.getContent());
//    assertThat(communityBoardResponse1.getCategory()).isEqualTo(communityBoard1.getCategory());
//
//  }
//
//  @Test
//  void updateCommunityBoard() {
//  }
//
//  @Test
//  void deleteCommunityBoard() {
//  }
//
//  @Test
//  void getCommunityBoardPage() {
//
//    //given
//    //빈 페이져블 객체 만들
//
//    //then
////empty넣고 empty인지 확인
//
//    //verify
//
//  }
//
//  @Test
//  void getCommunityBoard() {
//
//    //given
//    User user = new User();
//    //when
//    //Long만 필요하고 제대로된 값은 필요없을 때 anyLong()
//    CommunityBoardResponse response = communityBoardService.getCommunityBoard(anyLong(), user);
//    //then
//
//  }
//
//  @Test
//  void checkUser() {
//  }
//
//  @Test
//  void getCommunityBoardAndCheck() {
//  }
//
//  @Test
//  void countLike() {
//  }
//
//  @Test
//  void isExistBoard() {
//  }
//
//  @Test
//  void getNickname() {
//  }
//
//  @Test
//  void getNicknameByCommunityBoard() {
//  }
//}