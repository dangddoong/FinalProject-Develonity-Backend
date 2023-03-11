package com.develonity.common.auth;

import com.develonity.common.security.users.UserDetailsServiceAddEnum;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsFactory {
  private final List<UserDetailsServiceAddEnum> userDetailsServiceAddEnumList;
  private HashMap<UserDetailsServiceType, UserDetailsServiceAddEnum> serviceMap = new HashMap<>();

  public UserDetailsFactory(List<UserDetailsServiceAddEnum> userDetailsServiceAddEnumList) {
    this.userDetailsServiceAddEnumList = userDetailsServiceAddEnumList;
    for(UserDetailsServiceAddEnum userDetailsServiceAddEnum: userDetailsServiceAddEnumList){
      serviceMap.put(userDetailsServiceAddEnum.getServiceType(), userDetailsServiceAddEnum);
    }
  }

  public UserDetails getUserDetails(String loginId,UserDetailsServiceType userDetailsServiceType){
    UserDetailsServiceAddEnum userDetailsService = serviceMap.get(userDetailsServiceType);
        return userDetailsService.loadUserByUsername(loginId);
  }

}
