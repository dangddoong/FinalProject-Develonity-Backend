package com.develonity.common.security.users;

import com.develonity.common.auth.UserDetailsServiceType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserDetailsServiceAddEnum extends UserDetailsService {

  @Override
  UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
  UserDetailsServiceType getServiceType();
}
