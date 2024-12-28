package com.semangat.sukses.repository;

import com.semangat.sukses.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByAdminId(Long idAdmin);
}

