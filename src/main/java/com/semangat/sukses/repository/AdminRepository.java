package com.semangat.sukses.repository;

import com.semangat.sukses.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {

    @Query(value = "SELECT * FROM admin WHERE adminname = :adminname", nativeQuery = true)
    Optional<Admin> findByAdminname (String adminname);


    Optional<Admin> findByEmail (String adminname);
}