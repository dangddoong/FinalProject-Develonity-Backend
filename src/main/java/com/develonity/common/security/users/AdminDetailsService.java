package com.develonity.common.security.users;

import com.develonity.admin.entity.Admin;
import com.develonity.admin.repository.AdminRepository;
import com.develonity.common.auth.UserDetailsServiceType;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminDetailsService implements UserDetailsServiceAddEnum {

  private final AdminRepository adminRepository;

  @Override
  public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
    Admin admin = adminRepository.findByLoginId(loginId)
        .orElseThrow(() -> new UsernameNotFoundException("관리자를 찾을 수 없습니다."));
    return new AdminDetails(admin, loginId, admin.getId());
  }

  public UserDetailsServiceType getServiceType(){
    return UserDetailsServiceType.ADMIN;
  }
}

