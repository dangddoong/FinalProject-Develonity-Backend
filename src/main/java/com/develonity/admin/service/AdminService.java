package com.develonity.admin.service;

import com.develonity.admin.dto.RegisterResponse;
import com.develonity.admin.dto.userResponse;
import com.develonity.comment.dto.CommentResponse;

import java.util.List;

public interface AdminService {

    List<userResponse> findRegisterList(int page);

    List<RegisterResponse> findRegisterinfo();

    List<userResponse> findUsersRole();

    //List<CommentResponse>findRegisterComments(int page);

}
