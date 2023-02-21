package com.develonity.user.repository;


import com.develonity.user.entity.ProfileImage;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileImageRepository extends JpaRepository<ProfileImage, Long> {


  Optional<ProfileImage> findByUserId(Long userId);

//  ProfileImage findProfileImageByUserId(Long userId);

  void deleteByUserId(Long userId);

  boolean existsByUserId(Long userId);
}
