package com.dragonshoe.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.dragonshoe.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    
	
	@Query("SELECT DISTINCT p FROM Product p LEFT JOIN p.brand b WHERE p.productName LIKE %:keyword% OR b.brandName LIKE %:keyword%")
    List<Product> findByKeyword(String keyword);

	
}