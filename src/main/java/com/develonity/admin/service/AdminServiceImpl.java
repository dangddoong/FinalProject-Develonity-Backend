package com.develonity.admin.service;

import com.develonity.order.repository.GiftCardRepository;
import com.develonity.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminServiceImpl implements AdminService {

  private final UserRepository userRepository;
  private final GiftCardRepository giftCardRepository;

//    @Override
//    @Transactional
//    public List<AdminResponse> findRegisterList(int page) throws IllegalArgumentException {
//
//
//
//
//    }
}
