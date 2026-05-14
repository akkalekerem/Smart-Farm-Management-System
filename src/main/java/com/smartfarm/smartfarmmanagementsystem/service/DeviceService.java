package com.smartfarm.smartfarmmanagementsystem.service;

import com.smartfarm.smartfarmmanagementsystem.entity.Device;
import com.smartfarm.smartfarmmanagementsystem.repository.DeviceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DeviceService {

    private final DeviceRepository deviceRepository;

    // Tüm cihazları listelemek için kullandığımız metot
    public List<Device> getAllDevices() {
        return deviceRepository.findAll();
    }
}