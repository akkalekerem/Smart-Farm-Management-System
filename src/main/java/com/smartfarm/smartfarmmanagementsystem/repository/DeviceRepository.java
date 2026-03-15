package com.smartfarm.smartfarmmanagementsystem.repository;

import com.smartfarm.smartfarmmanagementsystem.entity.Device;
import com.smartfarm.smartfarmmanagementsystem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Long> {

    // Giriş yapan çiftçinin (User) sahip olduğu tüm cihazları liste halinde getirir
    List<Device> findByOwner(User owner);
}