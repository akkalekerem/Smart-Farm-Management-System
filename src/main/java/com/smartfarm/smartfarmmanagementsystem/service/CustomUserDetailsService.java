package com.smartfarm.smartfarmmanagementsystem.service;

import com.smartfarm.smartfarmmanagementsystem.entity.User;
import com.smartfarm.smartfarmmanagementsystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Formdan gelen e-postayı veritabanında arıyoruz
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Kullanıcı bulunamadı: " + email));

        // Spring Security'nin tanıdığı User nesnesine dönüştürüyoruz
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                new ArrayList<>() // Yetkilendirme şimdilik boş
        );
    }
}