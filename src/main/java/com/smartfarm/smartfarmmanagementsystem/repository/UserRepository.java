package com.smartfarm.smartfarmmanagementsystem.repository;

import com.smartfarm.smartfarmmanagementsystem.entity.User; // Paket adınızı kontrol edin
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Giriş yaparken kullanıcıyı e-postasına göre bulmamızı sağlayacak metod
    Optional<User> findByEmail(String email);
}