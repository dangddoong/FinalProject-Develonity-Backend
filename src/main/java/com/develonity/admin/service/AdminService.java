package com.develonity.admin.service;

import com.develonity.admin.dto.RegisterResponse;
import com.develonity.admin.dto.UserResponse;
import com.develonity.user.entity.User;
import com.develonity.user.entity.UserRole;
import org.springframework.data.domain.Page;




public interface AdminService {

    //1. 회원을 전체조회 하는데 (페이지에 실명, 닉네임, 로그인 아이디, 유저역할)
    Page<UserResponse> getAllUsers(User user);


    //2. 회원을 상세하게 해서 조회, (페이징 필요..x) 상세조회
    RegisterResponse getUsersInfo(Long id, User user);


    //3. 회원을 역할에 따라 조회( 일반, 전문, 어드민 )
    Page<UserResponse> getUsersAllByRole(UserRole userRole);



    //List<CommentResponse>findRegisterComments(int page);

}
