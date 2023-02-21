package com.develonity.user.repository;


import com.develonity.user.entity.ProfileImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileImageRepository extends JpaRepository<ProfileImage, Long> {


  ProfileImage findByUserId(Long userId);

  void deleteByUserId(Long userId);

  boolean existsByUserId(Long userId);
}
