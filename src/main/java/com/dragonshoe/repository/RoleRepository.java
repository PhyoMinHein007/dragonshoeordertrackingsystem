package com.dragonshoe.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dragonshoe.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByRoleName(String roleName);
}
