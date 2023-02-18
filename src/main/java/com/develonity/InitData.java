package com.develonity;

import com.develonity.admin.entity.Admin;
import com.develonity.admin.repository.AdminRepository;
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

  @Override
  public void run(ApplicationArguments args) throws Exception {
    Admin admin = new Admin("admin", passwordEncoder.encode("admin"));
    adminRepository.save(admin);
  }
}
