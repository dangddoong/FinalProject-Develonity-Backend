package com.develonity;

import com.develonity.admin.entity.Admin;
import com.develonity.admin.repository.AdminRepository;
import com.develonity.user.entity.User;
import com.develonity.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InitData implements ApplicationRunner {

  private final AdminRepository adminRepository;
  private final PasswordEncoder passwordEncoder;
  private final UserRepository userRepository;

  @Override
  public void run(ApplicationArguments args) throws Exception {
    Admin admin = new Admin("admin", passwordEncoder.encode("admin"));
    adminRepository.save(admin);
    User user = new User("test12", passwordEncoder.encode("aaAAaa1!"), "당뚱", "test12@naver.com");
    User user1 = new User("test123", passwordEncoder.encode("aaAAaa1!"), "성현", "test12@naver.co");
    userRepository.save(user);
    userRepository.save(user1);
  }
}
