package com.smartfarm.smartfarmmanagementsystem.config;

import com.smartfarm.smartfarmmanagementsystem.entity.Role;
import com.smartfarm.smartfarmmanagementsystem.entity.User;
import com.smartfarm.smartfarmmanagementsystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner initData() {
        return args -> {
            // Veritabanında admin e-postasıyla kayıtlı biri var mı bakıyoruz
            String adminEmail = "admin@example.com";

            if (userRepository.findByEmail(adminEmail).isEmpty()) {
                User admin = new User();
                admin.setFirstName("Sistem");
                admin.setLastName("Yöneticisi");
                admin.setEmail(adminEmail);
                // Şifreyi burada belirliyoruz
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setRole(Role.ADMIN); // Rolü ADMIN olarak set ediyoruz

                userRepository.save(admin);
                System.out.println("Sistem: İlk Admin kullanıcısı oluşturuldu (admin@example.com / admin123)");
            }
        };
    }
}