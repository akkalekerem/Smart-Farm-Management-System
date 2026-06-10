package com.smartfarm.smartfarmmanagementsystem.repository;

import com.smartfarm.smartfarmmanagementsystem.entity.Device;
import com.smartfarm.smartfarmmanagementsystem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
    // Giriş yapan çiftçinin (User) sahip olduğu tüm cihazları liste halinde getirir
    public interface DeviceRepository extends JpaRepository<Device, Long> {
        // 'user' yerine 'owner' yazmalısın (ya da Device.java'daki isim neyse o)
        List<Device> findByOwner(User user);
        long countByOwner(User user);
    }
