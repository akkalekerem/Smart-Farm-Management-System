package com.smartfarm.smartfarmmanagementsystem.controller;

import com.smartfarm.smartfarmmanagementsystem.entity.Device;
import com.smartfarm.smartfarmmanagementsystem.entity.User;
import com.smartfarm.smartfarmmanagementsystem.repository.DeviceRepository;
import com.smartfarm.smartfarmmanagementsystem.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*; // PathVariable ve diğerleri için

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class DeviceController {

    private final DeviceRepository deviceRepository;
    private final UserService userService;

    @GetMapping("/devices")
    public String userDevicesPage(Model model, Authentication authentication) {
        String email = authentication.getName();
        User currentUser = userService.findByEmail(email);
        List<Device> myDevices = deviceRepository.findByOwner(currentUser);

        model.addAttribute("devices", myDevices);
        model.addAttribute("activePage", "devices");
        return "pages/devices";
    }

    @PostMapping("/devices/add")
    public String addNewDevice(@RequestParam String deviceName,
                               @RequestParam String macAddress,
                               Authentication authentication) {
        String email = authentication.getName();
        User currentUser = userService.findByEmail(email);

        Device newDevice = new Device();
        newDevice.setDeviceName(deviceName);
        newDevice.setMacAddress(macAddress);
        newDevice.setOwner(currentUser);

        // Başlangıç değerleri (Mock data)
        newDevice.setOnline(true);
        newDevice.setBatteryLevel(100);
        newDevice.setWifiSignal(-45);
        newDevice.setCharging(false);
        newDevice.setLastUpdate(LocalDateTime.now());
        newDevice.setSoilMoistureActive(true);
        newDevice.setTempModuleActive(true);
        newDevice.setLightModuleActive(true);
        newDevice.setRainModuleActive(true);
        newDevice.setEcModuleActive(true);
        newDevice.setWindModuleActive(true);

        deviceRepository.save(newDevice);
        return "redirect:/devices";
    }

    // CİHAZ SİLME
    @PostMapping("/devices/delete/{id}")
    public String deleteDevice(@PathVariable("id") Long id) {
        deviceRepository.deleteById(id);
        return "redirect:/devices";
    }

    // CİHAZ GÜNCELLEME
    @PostMapping("/devices/update/{id}")
    public String updateDevice(@PathVariable("id") Long id,
                               @RequestParam String deviceName,
                               @RequestParam String macAddress) {
        Device device = deviceRepository.findById(id).orElseThrow();
        device.setDeviceName(deviceName);
        device.setMacAddress(macAddress);
        deviceRepository.save(device);
        return "redirect:/devices";
    }
}