package com.smartfarm.smartfarmmanagementsystem.service;

import com.smartfarm.smartfarmmanagementsystem.entity.Role;
import com.smartfarm.smartfarmmanagementsystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.smartfarm.smartfarmmanagementsystem.entity.User;
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
        //Her yeni kayıt olan kullanıcıya varsayılan olarak USER rolü atıyoruz
        user.setRole(Role.USER);

        return userRepository.save(user);
    }

    // E-posta adresine göre kullanıcı bulur
    public User findByEmail(String email) {
        // userRepository.findByEmail bir Optional döner, .orElse(null) ise:
        // Eğer kullanıcı varsa onu çıkarır, yoksa null döner.
        return userRepository.findByEmail(email).orElse(null);
    }
}