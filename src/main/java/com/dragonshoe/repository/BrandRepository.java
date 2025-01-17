package com.dragonshoe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dragonshoe.entity.Brand;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Long> {
   
}