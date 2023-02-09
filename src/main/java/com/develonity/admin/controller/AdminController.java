package com.develonity.admin.controller;

import com.develonity.admin.repository.AdminRepository;
import com.develonity.admin.service.AdminService;
import com.develonity.user.dto.RegisterRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
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

    // 전체회원 조회(아이디, 실명, 닉네임) -  dTO를 User에서 받아오는게 맞지않나?
    @GetMapping("/register")
    public List<RegisterResponse> findRegisterList(){
        return adminService.findRegisterList();
    }

    // 개인회원 조회(아이디, 실명, 닉네임, 비밀번호) 회원가입내역 전체조회로 Dto
    @GetMapping("/register/{id}")
    public List<RegisterResponse> findRegisterinfo(){
        adminService.findRegisterinfo();
    }

}
