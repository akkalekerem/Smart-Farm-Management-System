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

                // Parametre sırası: Name, minTemp, maxTemp, minMoisture, maxMoisture, minLight, maxLight, minEc, maxEc, maxWind, Note

                // Sebzeler
                cropRepository.save(new Crop(null, "Biber", 18.0, 30.0, 60.0, 80.0, 500.0, 1000.0, 1.8, 2.8, 30.0,
                        "Sıcaklık 15 derecenin altına düşmemelidir."));

                cropRepository.save(new Crop(null, "Patlıcan", 20.0, 35.0, 60.0, 75.0, 600.0, 1100.0, 2.2, 3.2, 25.0,
                        "Yüksek sıcaklık ve nemi sever."));

                cropRepository.save(new Crop(null, "Kabak", 15.0, 30.0, 65.0, 85.0, 450.0, 950.0, 1.5, 2.5, 35.0,
                        "Hızlı büyüme evresinde su ihtiyacı artar."));

                cropRepository.save(new Crop(null, "Havuç", 10.0, 25.0, 70.0, 85.0, 300.0, 700.0, 1.2, 2.0, 40.0,
                        "Gevşek toprak ve düzenli nem kritik önemdedir."));

                cropRepository.save(new Crop(null, "Ispanak", 5.0, 22.0, 60.0, 80.0, 300.0, 600.0, 1.0, 1.8, 45.0,
                        "Serin iklim sebzesidir, yüksek sıcaklıkta tohuma kaçar."));

                cropRepository.save(new Crop(null, "Marul", 10.0, 24.0, 65.0, 85.0, 350.0, 650.0, 1.2, 1.8, 30.0,
                        "Anlık nem değişimlerine karşı hassastır."));

                // Meyveler
                cropRepository.save(new Crop(null, "Kavun", 22.0, 38.0, 50.0, 70.0, 700.0, 1300.0, 2.0, 3.0, 40.0,
                        "Olgunlaşma döneminde bol güneş ve az nem ister."));

                cropRepository.save(new Crop(null, "Karpuz", 20.0, 35.0, 55.0, 75.0, 700.0, 1300.0, 1.8, 2.8, 40.0,
                        "Gelişim döneminde derin sulama gerektirir."));

                cropRepository.save(new Crop(null, "Armut", 15.0, 28.0, 60.0, 75.0, 500.0, 900.0, 1.5, 2.5, 30.0,
                        "Çiçeklenme döneminde don riskine dikkat edilmelidir."));

                cropRepository.save(new Crop(null, "Elma", 12.0, 26.0, 60.0, 75.0, 500.0, 1000.0, 1.5, 2.4, 35.0,
                        "Havadar ve güneşli bölgelerde meyve kalitesi artar."));

                // Hal bülteninde popüler olan egzotik meyveler
                cropRepository.save(new Crop(null, "Avokado", 18.0, 30.0, 65.0, 85.0, 600.0, 1100.0, 1.5, 2.2, 20.0,
                        "Rüzgara karşı çok hassastır, korunaklı alan ister."));

                cropRepository.save(new Crop(null, "Muz", 20.0, 35.0, 70.0, 90.0, 700.0, 1200.0, 2.0, 3.0, 15.0,
                        "Sürekli yüksek nem ve bol su gerektirir."));

                System.out.println("Sistem: Ürün Kütüphanesi yüklendi.");
            }
        };
    }
}