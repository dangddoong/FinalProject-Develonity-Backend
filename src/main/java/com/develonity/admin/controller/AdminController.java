package com.develonity.admin.controller;

import com.develonity.admin.dto.RegisterResponse;
import com.develonity.admin.dto.UserResponse;
import com.develonity.admin.repository.AdminUserRepository;
import com.develonity.admin.service.AdminService;
import com.develonity.common.security.users.UserDetailsImpl;
import com.develonity.user.entity.User;
import com.develonity.user.entity.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;




@Getter
@AllArgsConstructor
@RestController
@RequestMapping("/api/admins")
//admin이 한명이아니라 한번 생각
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AdminController {

  private final AdminUserRepository adminRepository;
  private final AdminService adminService;

  // 전체회원 조회(아이디, 실명, 닉네임, 롤) -  dTO를 User에서
  @GetMapping("/users")
  public Page<UserResponse> getAllUsers(@AuthenticationPrincipal UserDetailsImpl userDetails){
    return adminService.getAllUsers(userDetails.getUser());}


  // 개인회원 조회(아이디, 실명, 닉네임, 비밀번호) 회원가입내역 전체조회로 Dto
  @GetMapping("/users/{id}")
  public RegisterResponse getUsersinfo(@PathVariable Long id, User user){
    return adminService.getUsersInfo(id, user);}


  // 회원 role(일반,전문가,관리자) 분리하여 조회
  @GetMapping("/users/role/{userRole}")
  public Page<UserResponse> getUsersAllByRole(@PathVariable UserRole userRole){
    return adminService.getUsersAllByRole(userRole);
  }
}
