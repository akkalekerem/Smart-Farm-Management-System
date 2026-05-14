package com.smartfarm.smartfarmmanagementsystem.repository;

import com.smartfarm.smartfarmmanagementsystem.entity.Notification;
import com.smartfarm.smartfarmmanagementsystem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    // Kullanıcıya göre bildirimleri tarihe göre tersten (en yeni en üstte) listelemek için
    List<Notification> findByUserOrderByTimestampDesc(User user);

    // OKUNMAMIŞ BİLDİRİMLERİ BULMAK İÇİN (Hata veren eksik metot buydu)
    // Eğer Entity içinde değişken adın "isRead" ise bu isim tam uyar.
    List<Notification> findByUserAndIsReadFalse(User user);
}