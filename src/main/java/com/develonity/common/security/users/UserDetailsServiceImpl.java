package com.develonity.common.security.users;

import com.develonity.admin.entity.Admin;
import com.develonity.admin.repository.AdminRepository;
import com.develonity.user.entity.User;
import com.develonity.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

  private final UserRepository userRepository;
  private final AdminRepository adminRepository;

  @Override
  public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
    try {
      User user = userRepository.findByLoginId(loginId)
          .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
      return new UserDetailsImpl(user, loginId, user.getId());
    } catch (Exception e) {
      Admin admin = adminRepository.findByLoginId(loginId)
          .orElseThrow(() -> new UsernameNotFoundException("관리자를 찾을 수 없습니다."));
      return new AdminDetails(admin, loginId, admin.getId());
    }
  }
}
