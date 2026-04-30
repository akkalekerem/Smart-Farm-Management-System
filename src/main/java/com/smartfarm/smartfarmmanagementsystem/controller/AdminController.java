package com.smartfarm.smartfarmmanagementsystem.controller;

import com.smartfarm.smartfarmmanagementsystem.entity.User;
import com.smartfarm.smartfarmmanagementsystem.service.UserService;
import com.smartfarm.smartfarmmanagementsystem.service.DeviceService;
import com.smartfarm.smartfarmmanagementsystem.service.SystemMonitorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.smartfarm.smartfarmmanagementsystem.repository.TicketRepository;
import com.smartfarm.smartfarmmanagementsystem.entity.Ticket;

import java.util.Map;
import java.util.HashMap;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;
    private final DeviceService deviceService;
    private final SystemMonitorService systemMonitorService;
    private final TicketRepository ticketRepository;

    // ==========================================
    // 1. ADMIN ANA GİRİŞ SAYFASI
    // ==========================================
    @GetMapping("")
    public String adminIndex() {
        return "admin_dashboard";
    }

    // ==========================================
    // 2. KULLANICI LİSTESİ
    // ==========================================
    @GetMapping("/users")
    public String listUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "admin_users_list";
    }

    // ==========================================
    // 3. KULLANICI DETAY SAYFASI
    // ==========================================
    @GetMapping("/user/detail/{id}")
    public String userDetail(@PathVariable Long id, Model model) {
        User user = userService.findById(id);
        if (user == null) {
            return "redirect:/admin/users";
        }
        model.addAttribute("user", user);
        return "admin_user_detail";
    }

    // ==========================================
    // 4. KULLANICI SİLME İŞLEMİ
    // ==========================================
    @GetMapping("/user/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "redirect:/admin/users";
    }

    // ==========================================
    // 5. CİHAZ YÖNETİMİ (FİLO KONTROLÜ)
    // ==========================================
    @GetMapping("/devices")
    public String listDevices(Model model) {
        model.addAttribute("devices", deviceService.getAllDevices());
        return "admin_devices_list";
    }

    // ==========================================
    // 6. AĞ VE SİSTEM SAĞLIĞI (SAYFAYI AÇAN METOT)
    // ==========================================
    @GetMapping("/network")
    public String networkHealth() {
        // Bu metot sadece iskelet HTML'i (admin_network_health.html) yükler
        return "admin_network_health";
    }

    // ==========================================
    // 6.1 SİSTEM VERİLERİ (API ENDPOINT - JSON)
    // ==========================================
    @GetMapping("/api/network-data")
    @ResponseBody
    public Map<String, Object> getNetworkData() {
        // JavaScript (Fetch API) buraya istek atarak anlık verileri alır
        Map<String, Object> data = new HashMap<>();
        data.put("cpuUsage", systemMonitorService.getCpuUsage());
        data.put("usedRam", systemMonitorService.getUsedRam());
        data.put("totalRam", systemMonitorService.getTotalRam());
        return data;
    }

    // ==========================================
    // 7. DESTEK TALEPLERİ LİSTESİ
    // ==========================================
    @GetMapping("/support") // /admin/support adresini dinler
    public String listTickets(Model model) {
        // Tüm ticket'ları çekip sayfaya gönderiyoruz
        model.addAttribute("tickets", ticketRepository.findAllByOrderByCreatedAtDesc());
        return "admin_tickets_list"; // Bu isimde bir HTML oluşturacağız
    }
}