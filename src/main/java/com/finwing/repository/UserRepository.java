package com.finwing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.finwing.entity.User;
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
} 
