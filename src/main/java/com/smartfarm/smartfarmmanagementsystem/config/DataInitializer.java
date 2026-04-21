package com.smartfarm.smartfarmmanagementsystem.config;

import com.smartfarm.smartfarmmanagementsystem.entity.Crop;
import com.smartfarm.smartfarmmanagementsystem.entity.Role;
import com.smartfarm.smartfarmmanagementsystem.entity.User;
import com.smartfarm.smartfarmmanagementsystem.repository.CropRepository;
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
    private final CropRepository cropRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner initData() {
        return args -> {
            // 1. ADIM: ADMIN KULLANICISI (Varsa dokunmaz, yoksa oluşturur)
            String adminEmail = "admin@example.com";
            if (userRepository.findByEmail(adminEmail).isEmpty()) {
                User admin = new User();
                admin.setFirstName("Sistem");
                admin.setLastName("Yöneticisi");
                admin.setEmail(adminEmail);
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setRole(Role.ADMIN);
                userRepository.save(admin);
                System.out.println("Sistem: Admin hesabı hazır.");
            }

            // 2. ADIM: ÜRÜN KÜTÜPHANESİ (Tüm parametreler dahil)
            if (cropRepository.count() == 0) {
                // Parametre sırası: Name, minTemp, maxTemp, minMoisture, maxMoisture, minLight, maxLight, minEc, maxEc, maxWind, Note
                cropRepository.save(new Crop(null, "Patates", 15.0, 28.0, 60.0, 85.0, 400.0, 800.0, 1.5, 2.5, 45.0,
                        "Yumru gelişimi için nemi koruyun."));

                cropRepository.save(new Crop(null, "Domates", 18.0, 32.0, 50.0, 75.0, 600.0, 1200.0, 2.0, 3.5, 35.0,
                        "Bol ışık meyve kalitesini artırır."));

                cropRepository.save(new Crop(null, "Çilek", 12.0, 25.0, 65.0, 80.0, 500.0, 900.0, 1.0, 1.8, 20.0,
                        "Rüzgar ve yüksek sıcaklığa duyarlıdır."));

                System.out.println("Sistem: Ürün Kütüphanesi yüklendi.");
            }
        };
    }
}