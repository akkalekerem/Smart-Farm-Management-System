package com.smartfarm.smartfarmmanagementsystem.service;

import com.smartfarm.smartfarmmanagementsystem.entity.Role;
import com.smartfarm.smartfarmmanagementsystem.entity.User;
import com.smartfarm.smartfarmmanagementsystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // NOT: Eğer forum gönderilerini ve yorumlarını bu servis üzerinden
    // manuel silmek/çekmek istersen ileride buraya ForumPostRepository
    // ve ForumCommentRepository de ekleyebiliriz.

    // ==========================================
    // 1. CREATE (Oluşturma İşlemleri)
    // ==========================================
    public User registerUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.USER);
        return userRepository.save(user);
    }

    // ==========================================
    // 2. READ (Okuma / Listeleme İşlemleri)
    // ==========================================

    // Admin panelindeki tablo için tüm kullanıcıları getirir
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Kullanıcı detay sayfasına tıklandığında spesifik kişiyi getirir
    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    // E-postaya göre arama (Giriş işlemleri veya admin aramaları için)
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    // ==========================================
    // 3. UPDATE (Güncelleme İşlemleri)
    // ==========================================

    // Adminin bir kullanıcının rolünü, e-postasını veya adını değiştirmesi için
    public User updateUser(User user) {
        // save() metodu, eğer objenin ID'si varsa yeni kayıt açmaz, mevcut olanı günceller.
        return userRepository.save(user);
    }

    // ==========================================
    // 4. DELETE (Silme İşlemleri)
    // ==========================================

    @Transactional // İlişkili veriler silinirken hata çıkarsa işlemi geri alır (güvenlik için)
    public void deleteUser(Long userId) {
        // ÖNEMLİ NOT: Bir kullanıcıyı silmeden önce, veritabanı yapısına (Entity) göre
        // kullanıcının tarlalarını, forum postlarını ve yorumlarını ya silmen
        // ya da "Anonim Kullanıcı"ya devretmen gerekir.
        // Eğer User.java entity dosyasında gönderiler için CascadeType.ALL ve orphanRemoval=true
        // kullandıysan, bu satır otomatik olarak adamın her şeyini silecektir.

        userRepository.deleteById(userId);
    }
}