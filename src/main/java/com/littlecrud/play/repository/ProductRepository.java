package com.littlecrud.play.repository;

import com.littlecrud.play.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    List<Product> findByDeletedAtIsNull();
    List<Product> findByDeletedAtIsNotNull();
}