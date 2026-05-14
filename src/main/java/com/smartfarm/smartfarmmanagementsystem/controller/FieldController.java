package com.smartfarm.smartfarmmanagementsystem.controller;

import com.smartfarm.smartfarmmanagementsystem.entity.*;
import com.smartfarm.smartfarmmanagementsystem.repository.*;
import com.smartfarm.smartfarmmanagementsystem.service.UserService;
import com.smartfarm.smartfarmmanagementsystem.service.AiService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.HashMap;
import java.util.Locale;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Controller
@RequiredArgsConstructor
public class FieldController {

    private final FieldRepository fieldRepository;
    private final DeviceRepository deviceRepository;
    private final NotificationRepository notificationRepository;
    private final UserService userService;
    private final AiService aiService;

    private final Map<Long, TelemetryState> telemetryStates = new ConcurrentHashMap<>();

    private static class TelemetryState {
        double temp = 23.5;
        double moisture = 52.0;
        double light = 820.0;
        double ec = 1.35;
        double wind = 10.5;
        long lastAiCheckTime = 0;
    }

    @GetMapping("/")
    public String index(Model model, Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            User currentUser = userService.findByEmail(authentication.getName());
            model.addAttribute("fields", fieldRepository.findByOwner(currentUser));
            model.addAttribute("devices", deviceRepository.findByOwner(currentUser));
        }
        model.addAttribute("activePage", "home");
        return "pages/index";
    }
    
    @PostMapping("/fields/add")
    public String addField(@RequestParam String fieldName,
                           @RequestParam String cropType,
                           @RequestParam Double areaSize,
                           @RequestParam Long deviceId,
                           Authentication authentication) {

        User currentUser = userService.findByEmail(authentication.getName());
        Device device = deviceRepository.findById(deviceId).orElse(null);

        Field newField = new Field();
        newField.setFieldName(fieldName);
        newField.setCropType(cropType);
        newField.setAreaSize(areaSize);
        newField.setOwner(currentUser);
        newField.setDevice(device);

        fieldRepository.save(newField);
        return "redirect:/";
    }

    // --- TARLA SİLME (404 HATASINI ÇÖZEN KISIM) ---
    @PostMapping("/fields/delete/{id}")
    public String deleteField(@PathVariable("id") Long id) {
        fieldRepository.deleteById(id);
        telemetryStates.remove(id); // Hafızayı temizle
        return "redirect:/";
    }

    // --- TARLA GÜNCELLEME (HATA ALMAMAK İÇİN EKLENDİ) ---
    @PostMapping("/fields/update/{id}")
    public String updateField(@PathVariable("id") Long id,
                              @RequestParam String fieldName,
                              @RequestParam String cropType,
                              @RequestParam Double areaSize) {

        Field field = fieldRepository.findById(id).orElseThrow();
        field.setFieldName(fieldName);
        field.setCropType(cropType);
        field.setAreaSize(areaSize);

        fieldRepository.save(field);
        return "redirect:/";
    }

    @GetMapping("/fields/{id}")
    public String fieldDetails(@PathVariable("id") Long id, Model model) {
        Field field = fieldRepository.findById(id).orElseThrow();
        model.addAttribute("field", field);
        model.addAttribute("activePage", "home");
        return "pages/field_detail";
    }

    // --- TELEMETRİ VE AI API ---
    @GetMapping("/api/fields/{id}/telemetry")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getSimulatedTelemetry(@PathVariable("id") Long id, Authentication authentication) {
        Field field = fieldRepository.findById(id).orElseThrow();
        User currentUser = userService.findByEmail(authentication.getName());

        TelemetryState state = telemetryStates.computeIfAbsent(id, k -> new TelemetryState());

        state.temp += (Math.random() - 0.5) * 0.6;
        state.moisture += (Math.random() - 0.5) * 1.0;
        state.light += (Math.random() - 0.5) * 30.0;
        state.ec += (Math.random() - 0.5) * 0.02;
        state.wind += (Math.random() - 0.5) * 2.0;

        state.temp = Math.max(10.0, Math.min(42.0, state.temp));
        state.moisture = Math.max(20.0, Math.min(95.0, state.moisture));

        long currentTime = System.currentTimeMillis();
        if (currentTime - state.lastAiCheckTime > 15000) {
            String aiComment = aiService.getFarmInsight(field.getFieldName(), field.getCropType(), state.temp, state.moisture, state.wind);
            if (aiComment != null && !aiComment.isEmpty()) {
                createNotification(currentUser, "🤖 AI Danışman: " + aiComment);
            }
            state.lastAiCheckTime = currentTime;
        }

        Map<String, Object> data = new HashMap<>();
        data.put("temperature", String.format(Locale.US, "%.1f", state.temp));
        data.put("soilMoisture", String.format(Locale.US, "%.1f", state.moisture));
        data.put("lightLevel", String.format(Locale.US, "%.0f", state.light));
        data.put("ecLevel", String.format(Locale.US, "%.2f", state.ec));
        data.put("windSpeed", String.format(Locale.US, "%.1f", state.wind));
        return ResponseEntity.ok(data);
    }

    private void createNotification(User user, String message) {
        Notification n = new Notification();
        n.setMessage(message);
        n.setTimestamp(LocalDateTime.now());
        n.setUser(user);
        n.setRead(false);
        notificationRepository.save(n);
    }
}