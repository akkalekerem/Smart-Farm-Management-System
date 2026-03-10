package com.smartfarm.smartfarmmanagementsystem.service;

import com.smartfarm.smartfarmmanagementsystem.entity.User;
import com.smartfarm.smartfarmmanagementsystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // Yeni kullanıcıyı şifreleyerek kaydeder
    public User registerUser(User user) {
        // Şifreyi BCrypt ile güvenli hale getiriyoruz
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    // E-posta adresine göre kullanıcı bulur
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}