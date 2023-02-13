package com.develonity.admin.dto;

import com.develonity.user.entity.User;
import com.develonity.user.entity.UserRole;


public class RegisterResponse {
    private final String loginId;
    private final String password;
    private final String realName;
    private final String nickName;
    private final String profileImageUrl;
    private final String email;
    private final String phoneNumber;
    private final User.Address address;
    private final UserRole userRole;
    public RegisterResponse(User user) {
        this.loginId = user.getLoginId();
        this.password = user.getPassword();
        this.realName = user.getRealName();
        this.nickName = user.getNickName();
        this.profileImageUrl = user.getProfileImageUrl();
        this.email = user.getEmail();
        this.phoneNumber = user.getPhoneNumber();
        this.address = user.getAddress();
        this.userRole = user.getUserRole();
    }
}
