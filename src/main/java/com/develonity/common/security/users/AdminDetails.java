package com.develonity.common.security.users;

import com.develonity.admin.entity.Admin;
import com.develonity.admin.entity.AdminRole;
import java.util.ArrayList;
import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class AdminDetails implements UserDetails {

  private final Admin admin;
  private final String username;
  private final Long adminId;

  public AdminDetails(Admin admin, String username, Long adminId) {
    this.admin = admin;
    this.username = username;
    this.adminId = adminId;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    AdminRole role = admin.getRole();
    String authority = role.getAuthority();
    SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(authority);
    Collection<GrantedAuthority> authorities = new ArrayList<>();
    authorities.add(simpleGrantedAuthority);
    return authorities;
  }

  @Override
  public String getPassword() {
    return admin.getPassword();
  }

  @Override
  public String getUsername() {
    return username;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
