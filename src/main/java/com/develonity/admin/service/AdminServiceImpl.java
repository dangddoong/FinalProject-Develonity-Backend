package com.develonity.admin.service;

import com.develonity.admin.dto.RegisterResponse;
import com.develonity.admin.dto.UserResponse;
import com.develonity.admin.repository.AdminUserRepository;
import com.develonity.user.entity.User;
import com.develonity.user.entity.UserRole;
import com.develonity.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



import static com.develonity.common.exception.ExceptionStatus.PAGINATION_IS_NOT_EXIST;


@Service
@RequiredArgsConstructor
@Transactional
public class AdminServiceImpl implements com.develonity.admin.service.AdminService {

  private final AdminUserRepository adminUserRepository;
  private final UserRepository userRepository;


  //1.회원 리스트로 해서 페이징에다가 전체조회(loginId, realName,Nickname,UserRole)
  @Override
  @Transactional(readOnly = true)
  public Page<UserResponse> getAllUsers(User user) {
    Page<User> userPage = adminUserRepository.findAll(PageRequest.of(0, 20));
    return userPage.map(UserResponse::new);
  }

  //return userPage.stream().map(UserResponse::new).collect(Collectors.toList()); {

  //2. 회원을 상세하게 해서 조회, (페이징 필요..x) 상세조회
  @Override
  @Transactional
  public RegisterResponse getUsersInfo(Long id, User user) {
    user = userRepository.findById(id).orElseThrow(IllegalArgumentException::new);
    return new RegisterResponse(user);
  }

  //RegisterResponse registerResponse = userById.stream().map(UserResponse::new).collect(Collectors.toList());
  // 3. 롤에따라 상세하게 해서 조회(페이징 필수)
  @Override
  @Transactional(readOnly = true)
  public Page<UserResponse> getUsersAllByRole(UserRole userRole) {
    Page<User> users = adminUserRepository.findByUserRole(userRole,PageRequest.of(0, 20));
    return users.map(UserResponse::new);


    // 리팩토링할떄 pagedto 사용시 로직 참고
    //if (userResponse.getUserRole().equals(UserRole.AMATEUR)) {
    //  users = adminUserRepository.findByUserRole(UserRole.AMATEUR);
    //} else if (userResponse.getUserRole().equals(UserRole.EXPERT)) {
    //    users = adminUserRepository.findByUserRole(UserRole.EXPERT);
    //} else if (userResponse.getUserRole().equals(UserRole.ADMIN)) {
    //    users = adminUserRepository.findByUserRole(UserRole.ADMIN);
    //} else {
    //    throw new CustomException(PAGINATION_IS_NOT_EXIST);
    //}
    //return users.map(UserResponse::new);

  }

}
