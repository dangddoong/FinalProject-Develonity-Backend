package com.develonity.admin.service;

import com.develonity.admin.dto.RegisterResponse;
import com.develonity.admin.dto.userResponse;
import com.develonity.comment.dto.CommentResponse;

import java.util.List;

public interface AdminService {

    List<userResponse> findUsersList(int page);

    List<RegisterResponse> findUsersInfo();

    Page<userResponse> getUsersAllByRole(int page, int size, boolean isAsc, String role);

    //List<CommentResponse>findRegisterComments(int page);

}
