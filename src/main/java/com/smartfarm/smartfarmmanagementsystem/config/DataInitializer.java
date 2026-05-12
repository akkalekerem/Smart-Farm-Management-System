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
            // Admin Kullanıcısı Kontrolü
            if (userRepository.findByEmail("admin@example.com").isEmpty()) {
                User admin = new User();
                admin.setFirstName("Sistem");
                admin.setLastName("Yöneticisi");
                admin.setEmail("admin@example.com");
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setRole(Role.ADMIN);
                userRepository.save(admin);
            }

            // Ürün Kütüphanesi Yüklemesi (Fiyat bilgileri temizlendi)
            // 2. ADIM: ÜRÜN KÜTÜPHANESİ (Sadece Teknik Notlar)
            // 2. ADIM: ÜRÜN KÜTÜPHANESİ (Sadece Teknik Notlar)
            // 2. ADIM: ÜRÜN KÜTÜPHANESİ (Sadece Teknik ve Biyolojik Notlar)
            if (cropRepository.count() <= 3) {
                cropRepository.deleteAll(); // Mevcut hatalı (fiyat içeren) verileri temizlemek için

                // Sebzeler
                cropRepository.save(new Crop(null, "Domates", 18.0, 32.0, 50.0, 75.0, 600.0, 1200.0, 2.0, 3.5, 35.0, "Bol ışık meyve kalitesini artırır; düzenli budama önerilir."));
                cropRepository.save(new Crop(null, "Patates", 15.0, 28.0, 60.0, 85.0, 400.0, 800.0, 1.5, 2.5, 45.0, "Yumru gelişimi için toprak nemi kritik seviyede tutulmalıdır."));
                cropRepository.save(new Crop(null, "Biber", 18.0, 30.0, 60.0, 80.0, 500.0, 1000.0, 1.8, 2.8, 30.0, "Gece sıcaklıklarının 15 derecenin altına düşmemesine dikkat edilmelidir."));
                cropRepository.save(new Crop(null, "Patlıcan", 20.0, 35.0, 60.0, 75.0, 600.0, 1100.0, 2.2, 3.2, 25.0, "Sıcağı ve azot bakımından zengin toprakları sever."));
                cropRepository.save(new Crop(null, "Kabak", 15.0, 30.0, 65.0, 85.0, 450.0, 950.0, 1.5, 2.5, 35.0, "Hızlı gelişim süreci nedeniyle sık sulama ihtiyacı duyar."));
                cropRepository.save(new Crop(null, "Havuç", 10.0, 25.0, 70.0, 85.0, 300.0, 700.0, 1.2, 2.0, 40.0, "Gevşek ve taşsız topraklarda daha düzgün gelişim gösterir."));
                cropRepository.save(new Crop(null, "Ispanak", 5.0, 22.0, 60.0, 80.0, 300.0, 600.0, 1.0, 1.8, 45.0, "Serin iklimlerde verimi artar; yüksek sıcaklıkta tohuma kaçma riski vardır."));
                cropRepository.save(new Crop(null, "Marul", 10.0, 24.0, 65.0, 85.0, 350.0, 650.0, 1.2, 1.8, 30.0, "Anlık kuraklığa karşı çok hassastır, yapraklarda pörsüme yapabilir."));

                // Meyveler
                cropRepository.save(new Crop(null, "Çilek", 12.0, 25.0, 65.0, 80.0, 500.0, 900.0, 1.0, 1.8, 20.0, "Hassas kök yapısı nedeniyle drenajı iyi topraklar seçilmelidir."));
                cropRepository.save(new Crop(null, "Kavun", 22.0, 38.0, 50.0, 70.0, 700.0, 1300.0, 2.0, 3.0, 40.0, "Hasat dönemine yakın sulamanın azaltılması aromayı artırır."));
                cropRepository.save(new Crop(null, "Karpuz", 20.0, 35.0, 55.0, 75.0, 700.0, 1300.0, 1.8, 2.8, 40.0, "Gelişim başında bol su, olgunlaşmada yüksek güneş ışığı ister."));
                cropRepository.save(new Crop(null, "Armut", 15.0, 28.0, 60.0, 75.0, 500.0, 900.0, 1.5, 2.5, 30.0, "Kireçli ve derin topraklarda verimi yüksektir."));
                cropRepository.save(new Crop(null, "Elma", 12.0, 26.0, 60.0, 75.0, 500.0, 1000.0, 1.5, 2.4, 35.0, "Yıllık budama ve güneşlenme meyve rengini doğrudan etkiler."));
                cropRepository.save(new Crop(null, "Muz", 20.0, 35.0, 70.0, 90.0, 700.0, 1200.0, 2.0, 3.0, 15.0, "Yüksek nem ve rüzgardan korunmuş alanlar hayati önemdedir."));

                System.out.println("Sistem: Teknik Biyolojik Rehber yüklendi.");
            }
        };
    }
}