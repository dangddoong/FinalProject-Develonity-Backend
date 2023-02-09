package com.develonity.admin.controller;

import com.develonity.admin.repository.AdminRepository;
import com.develonity.admin.service.AdminService;
import com.develonity.comment.dto.CommentResponse;
import com.develonity.user.dto.RegisterRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Getter
@AllArgsConstructor
@RestController("/api/admins")
//admin이 한명이아니라 한번 생각
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AdminController {

    private final AdminRepository adminRepository;
    private final AdminService adminService;

    // 전체회원 조회(아이디, 실명, 닉네임) -  dTO를 User에서
    @GetMapping("/register")// 명사로 registration / enrollement
    public List<LoginResponse> findRegisterList(){
        return adminService.findRegisterList();
    }

    // 개인회원 조회(아이디, 실명, 닉네임, 비밀번호) 회원가입내역 전체조회로 Dto
    @GetMapping("/register/{id}")
    public List<RegisterResponse> findRegisterinfo(){
        adminService.findRegisterinfo();
    }

    // 어드민이 유저의 전체댓글 조회(admin입장에서 필요하다고 생각)
    @GetMapping ("/register/{id}/comments")
    public List<CommentResponse> findRegisterComments(){adminService.findRegisterComments();}

    // 어드민 유저 전체 댓글 중 댓글수정....
    @PutMapping("/register/{id}/comments/updates")
    public List<CommentResponse> updateRegisterComments(){adminService.updateRegisterComments();}
    public


    //order목록, 상품목록 추가 (관련 로직 설정 필요)
    // @PutMapping("/order")
}
