package com.develonity.common.auth;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsFactory {
  private final Map<String, UserDetailsService> userDetailsServiceMap;
  public UserDetails getUserDetails(String loginId,UserDetailsServiceType userDetailsServiceType){
    UserDetailsService userDetailsService = null;
    switch (userDetailsServiceType){
      case USER :
        userDetailsService = userDetailsServiceMap.get("userDetailsServiceImpl");
        break;
      case ADMIN :
        userDetailsService = userDetailsServiceMap.get("adminDetailsService");
        break;
      default :
        throw new IllegalStateException("Unexpected value: " + userDetailsServiceType);
    }
    return userDetailsService.loadUserByUsername(loginId);
  }

}
