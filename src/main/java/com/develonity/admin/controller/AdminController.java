package com.develonity.admin.controller;

import com.develonity.admin.service.AdminService;
import com.develonity.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

@Getter
@AllArgsConstructor
@RestController("/api/admins")
//admin이 한명이아니라 한번 생각
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AdminController {

  private final UserRepository userRepository;
  private final AdminService adminService;

  // 전체회원 조회(아이디, 실명, 닉네임, 롤) -  dTO를 User에서
//    @GetMapping("/users")
//    public List<userResponse> findUsersList(){return adminService.findUserList();}

  // 개인회원 조회(아이디, 실명, 닉네임, 비밀번호) 회원가입내역 전체조회로 Dto
//    @GetMapping("/users/{id}")
//    public List<RegisterResponse> findUsersinfo(){return adminService.findUsersinfo();}

  // 어드민이 유저의 전체댓글 조회(admin입장에서 필요하다고 생각)
//  @GetMapping("/users/{id}/comments")
//  public List<CommentResponse> findRegisterComments() {
//    return adminService.findRegisterComments();
//  }

  // 어드민 유저 전체 댓글 삭제....
  //@DeleteMapping("/register/{id}/comments/delete")
  //public List<CommentResponse> deleteRegisterComments(){return adminService.deleteRegisterComments();}

  //order목록, 상품목록 추가 (관련 로직 설정 필요)
  // @PutMapping("/order")
}
