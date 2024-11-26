package com.dragonshoe.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dragonshoe.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
}
