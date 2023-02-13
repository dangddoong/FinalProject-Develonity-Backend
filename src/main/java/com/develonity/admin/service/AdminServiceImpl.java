package com.develonity.admin.service;

import com.develonity.admin.dto.RegisterResponse;
import com.develonity.admin.dto.userResponse;
import com.develonity.order.repository.GiftCardRepository;
import com.develonity.user.entity.User;
import com.develonity.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Id;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminServiceImpl implements AdminService{

    private final UserRepository userRepository;

    //private final GiftCardRepository giftCardRepository;

    @Override
    @Transactional
    public List<userResponse> getUsersList(int page) throws IllegalArgumentException {
    Pageable pageable = PageRequest.of(page-1,10, Sort.Direction.DESC, "id");
    <User> users = userRepository.findByLoginId(id);
    List<userResponse> totalusers = users.stream().map(userResponse::new).collect(Collectors.toList());
    return totalusers;
    }


    @Override
    @Transactional
    public List<RegisterResponse> findUsersInfo() throws IllegalArgumentException{



        return eachusers
    }

}
