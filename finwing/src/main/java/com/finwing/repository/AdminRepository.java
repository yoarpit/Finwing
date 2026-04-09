package com.finwing.repository;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.finwing.entity.Admin;
public interface AdminRepository extends JpaRepository<Admin, Long>{
    Optional<Admin> findByAdminName( String adminName);

    Optional<Admin> findByEmail(String email);
boolean existsByAdminName(String adminName);
    boolean existsByEmail(String email);
}