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
            // 2. ADIM: ÜRÜN KÜTÜPHANESİ (Fotoğraf Linkleri İçin Güncellendi)
            if (cropRepository.count() <= 3) {
                cropRepository.deleteAll();

                // Parametre sırası: ID(null), İsim, MinT, MaxT, MinN, MaxN, MinI, MaxI, MinEC, MaxEC, MaxRuzgar, Fotoğraf_URL, Not

                // Sebzeler
                cropRepository.save(new Crop(null, "Domates", 18.0, 32.0, 50.0, 75.0, 600.0, 1200.0, 2.0, 3.5, 35.0,
                        "https://www.aftamarket.com.tr/sebze-38-sebzeler-manav-23805-26-B.jpg", "Bol ışık meyve kalitesini artırır; düzenli budama önerilir."));

                cropRepository.save(new Crop(null, "Patates", 15.0, 28.0, 60.0, 85.0, 400.0, 800.0, 1.5, 2.5, 45.0,
                        "https://static.ticimax.cloud/3140/uploads/urunresimleri/buyuk/30f3db45-8170-48cf-983e-be5c798df412.jpg", "Yumru gelişimi için toprak nemi kritik seviyede tutulmalıdır."));

                cropRepository.save(new Crop(null, "Biber", 18.0, 30.0, 60.0, 80.0, 500.0, 1000.0, 1.8, 2.8, 30.0,
                        "https://images.migrosone.com/sanalmarket/product/28054000/28054000_1-e26f7f-1650x1650.jpg", "Gece sıcaklıklarının 15 derecenin altına düşmemesine dikkat edilmelidir."));

                cropRepository.save(new Crop(null, "Patlıcan", 20.0, 35.0, 60.0, 75.0, 600.0, 1100.0, 2.2, 3.2, 25.0,
                        "https://static.ticimax.cloud/cdn-cgi/image/width=-,quality=85/11919/uploads/urunresimleri/buyuk/bostan-patlican-kg-2afc.png", "Sıcağı ve azot bakımından zengin toprakları sever."));

                cropRepository.save(new Crop(null, "Kabak", 15.0, 30.0, 65.0, 85.0, 450.0, 950.0, 1.5, 2.5, 35.0,
                        "https://ardenmarket.com.tr/media/catalog/product/cache/ce320e98947e5c83f08a8e256dc8423e/k/a/kabak_dolmalik_1.png", "Hızlı gelişim süreci nedeniyle sık sulama ihtiyacı duyar."));

                cropRepository.save(new Crop(null, "Havuç", 10.0, 25.0, 70.0, 85.0, 300.0, 700.0, 1.2, 2.0, 40.0,
                        "https://images.cagri.com/product/3495/1735266335457-4300-2910015.jpg", "Gevşek ve taşsız topraklarda daha düzgün gelişim gösterir."));

                cropRepository.save(new Crop(null, "Ispanak", 5.0, 22.0, 60.0, 80.0, 300.0, 600.0, 1.0, 1.8, 45.0,
                        "https://cdn.dsmcdn.com/mnresize/420/620/ty269/product/media/images/20211213/23/10248011/336901830/1/1_org_zoom.jpg", "Serin iklimlerde verimi artar; yüksek sıcaklıkta tohuma kaçma riski vardır."));

                cropRepository.save(new Crop(null, "Marul", 10.0, 24.0, 65.0, 85.0, 350.0, 650.0, 1.2, 1.8, 30.0,
                        "https://cdn-image.getir.com/market/product/44739124-2724-449d-83c9-516698f930b6.png", "Anlık kuraklığa karşı çok hassastır, yapraklarda pörsüme yapabilir."));

                // Meyveler
                cropRepository.save(new Crop(null, "Çilek", 12.0, 25.0, 65.0, 80.0, 500.0, 900.0, 1.0, 1.8, 20.0,
                        "https://static.ticimax.cloud/3140/Uploads/UrunResimleri/cilek-bursa-350-gr-60942c.jpg", "Hassas kök yapısı nedeniyle drenajı iyi topraklar seçilmelidir."));

                cropRepository.save(new Crop(null, "Kavun", 22.0, 38.0, 50.0, 70.0, 700.0, 1300.0, 2.0, 3.0, 40.0,
                        "https://images.migrosone.com/sanalmarket/product/27202000/27202000_1-8df405-1650x1650.jpg", "Hasat dönemine yakın sulamanın azaltılması aromayı artırır."));

                cropRepository.save(new Crop(null, "Karpuz", 20.0, 35.0, 55.0, 75.0, 700.0, 1300.0, 1.8, 2.8, 40.0,
                        "https://cdn.dsmcdn.com/mnresize/420/620/ty1001/product/media/images/prod/SPM/PIM/20230914/13/bb661db1-86e8-34a6-846b-bb2dff919972/1_org_zoom.jpg", "Gelişim başında bol su, olgunlaşmada yüksek güneş ışığı ister."));

                cropRepository.save(new Crop(null, "Armut", 15.0, 28.0, 60.0, 75.0, 500.0, 900.0, 1.5, 2.5, 30.0,
                        "https://www.biosantarim.com/tema/genel/uploads/urunler/armut.png", "Kireçli ve derin topraklarda verimi yüksektir."));

                cropRepository.save(new Crop(null, "Elma", 12.0, 26.0, 60.0, 75.0, 500.0, 1000.0, 1.5, 2.4, 35.0,
                        "https://cdn.cimri.io/market/260x260/kirmizi-elma-_224542.jpg", "Yıllık budama ve güneşlenme meyve rengini doğrudan etkiler."));

                cropRepository.save(new Crop(null, "Muz", 20.0, 35.0, 70.0, 90.0, 700.0, 1200.0, 2.0, 3.0, 15.0,
                        "https://cdn.dsmcdn.com/mnresize/420/620/ty195/product/media/images/20211012/14/145854367/261121340/1/1_org_zoom.jpg", "Yüksek nem ve rüzgardan korunmuş alanlar hayati önemdedir."));

                System.out.println("Sistem: Teknik Biyolojik Rehber yüklendi.");
            }
        };
    }
}