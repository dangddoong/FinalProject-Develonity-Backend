package com.develonity.user.dto;

import com.azul.crs.client.models.Address;
import com.develonity.user.entity.User;
import com.develonity.user.entity.UserRole;

public class SignupRequest {
    private Long id;

    private UserRole userRole;

    private String loginId;

    private String password;

    private String realname;

    private String nickname;

    private String profileImageUrl;

    private String email;

    private String phoneNumber;

    private Address address;
}
