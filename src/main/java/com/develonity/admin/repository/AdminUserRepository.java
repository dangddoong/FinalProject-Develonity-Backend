package com.develonity.admin.repository;

import com.develonity.user.entity.User;
import com.develonity.user.entity.UserRole;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;




@Repository
public interface AdminUserRepository extends JpaRepository<User, Long> {
    Page<User> findByUserRole(UserRole userRole,Pageable pageable);
}



