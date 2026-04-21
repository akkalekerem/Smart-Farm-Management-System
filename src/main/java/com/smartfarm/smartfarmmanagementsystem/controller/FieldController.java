package com.smartfarm.smartfarmmanagementsystem.controller;

import com.smartfarm.smartfarmmanagementsystem.entity.*;
import com.smartfarm.smartfarmmanagementsystem.repository.*;
import com.smartfarm.smartfarmmanagementsystem.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import com.smartfarm.smartfarmmanagementsystem.repository.NotificationRepository;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.HashMap;
import java.util.Locale;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class FieldController {

    private final FieldRepository fieldRepository;
    private final DeviceRepository deviceRepository;
    private final CropRepository cropRepository;
    private final NotificationRepository notificationRepository; // Bildirimleri kaydetmek için eklendi
    private final UserService userService;

    // TARLALARIM SEKMESİ
    @GetMapping("/")
    public String index(Model model, Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            User currentUser = userService.findByEmail(authentication.getName());

            List<Field> myFields = fieldRepository.findByOwner(currentUser);
            List<Device> myDevices = deviceRepository.findByOwner(currentUser);
            List<Crop> allCrops = cropRepository.findAll();

            model.addAttribute("fields", myFields);
            model.addAttribute("devices", myDevices);
            model.addAttribute("allCrops", allCrops);
        }

        model.addAttribute("activePage", "home");
        return "pages/index";
    }

    // YENİ TARLA EKLEME
    @PostMapping("/fields/add")
    public String addField(@RequestParam String fieldName,
                           @RequestParam Long cropId,
                           @RequestParam Double areaSize,
                           @RequestParam Long deviceId,
                           Authentication authentication) {

        User currentUser = userService.findByEmail(authentication.getName());
        Device selectedDevice = deviceRepository.findById(deviceId).orElse(null);
        Crop selectedCrop = cropRepository.findById(cropId).orElse(null);

        Field newField = new Field();
        newField.setFieldName(fieldName);
        newField.setCrop(selectedCrop);
        newField.setAreaSize(areaSize);
        newField.setOwner(currentUser);
        newField.setDevice(selectedDevice);

        fieldRepository.save(newField);
        return "redirect:/";
    }

    // TARLA SİLME
    @PostMapping("/fields/delete/{id}")
    public String deleteField(@PathVariable("id") Long id) {
        fieldRepository.deleteById(id);
        return "redirect:/";
    }

    // TARLA GÜNCELLEME
    @PostMapping("/fields/update/{id}")
    public String updateField(@PathVariable("id") Long id,
                              @RequestParam String fieldName,
                              @RequestParam Long cropId,
                              @RequestParam Double areaSize) {

        Field field = fieldRepository.findById(id).orElseThrow();
        Crop selectedCrop = cropRepository.findById(cropId).orElseThrow();

        field.setFieldName(fieldName);
        field.setCrop(selectedCrop);
        field.setAreaSize(areaSize);
        fieldRepository.save(field);

        return "redirect:/";
    }

    // DETAY KISMI
    @GetMapping("/fields/{id}")
    public String fieldDetails(@PathVariable("id") Long id, Model model) {
        Field field = fieldRepository.findById(id).orElseThrow();
        model.addAttribute("field", field);
        model.addAttribute("activePage", "home");
        return "pages/field_detail";
    }

    // BİLDİRİM DESTEKLİ TELEMETRİ API
    @GetMapping("/api/fields/{id}/telemetry")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getSimulatedTelemetry(@PathVariable("id") Long id, Authentication authentication) {
        Field field = fieldRepository.findById(id).orElseThrow();
        Crop crop = field.getCrop();
        User currentUser = userService.findByEmail(authentication.getName());

        // Sensör verisi simülasyonu
        double temp = 10.0 + (Math.random() * 30.0);
        double moisture = 20.0 + (Math.random() * 70.0);
        double light = 100.0 + (Math.random() * 1200.0);
        double ec = 0.5 + (Math.random() * 4.0);
        double wind = Math.random() * 60.0;
        boolean isRaining = Math.random() > 0.8;

        // BİLDİRİM MANTIĞI (Veritabanına Kayıt)
        if (crop != null) {
            checkAndNotify(field, currentUser, temp, moisture, wind, ec);
        }

        Map<String, Object> telemetryData = new HashMap<>();
        telemetryData.put("temperature", String.format(Locale.US, "%.1f", temp));
        telemetryData.put("soilMoisture", String.format(Locale.US, "%.1f", moisture));
        telemetryData.put("lightLevel", String.format(Locale.US, "%.0f", light));
        telemetryData.put("ecLevel", String.format(Locale.US, "%.2f", ec));
        telemetryData.put("windSpeed", String.format(Locale.US, "%.1f", wind));
        telemetryData.put("isRaining", isRaining);
        // aiInsight alanı silindi çünkü artık bildirim olarak kaydediliyor.

        return ResponseEntity.ok(telemetryData);
    }

    // Bildirimleri Kontrol Eden ve Kaydeden Yardımcı Metot
    private void checkAndNotify(Field field, User user, double temp, double moisture, double wind, double ec) {
        Crop crop = field.getCrop();

        // Sıcaklık Bildirimi
        if (temp > crop.getMaxTemp()) {
            createNotification(user, "🔥 " + field.getFieldName() + " tarlanızda yüksek sıcaklık: " + String.format("%.1f", temp) + "°C");
        } else if (temp < crop.getMinTemp()) {
            createNotification(user, "❄️ " + field.getFieldName() + " tarlanızda don riski: " + String.format("%.1f", temp) + "°C");
        }

        // Rüzgar Bildirimi
        if (wind > crop.getMaxWindSpeed()) {
            createNotification(user, "🌪️ Şiddetli Rüzgar! " + field.getFieldName() + " bölgesinde rüzgar hızı: " + String.format("%.1f", wind) + " km/h");
        }

        // Nem Bildirimi
        if (moisture < crop.getMinMoisture()) {
            createNotification(user, "💧 " + field.getFieldName() + " için acil sulama gerekli! Toprak nemi: %" + String.format("%.0f", moisture));
        }

        // EC (Besin) Bildirimi
        if (ec > crop.getMaxEc()) {
            createNotification(user, "⚠️ " + field.getFieldName() + " toprağında yüksek tuzluluk/besin birikimi tespit edildi.");
        }
    }

    private void createNotification(User user, String message) {
        Notification notification = new Notification();
        notification.setMessage(message);
        notification.setTimestamp(LocalDateTime.now());
        notification.setUser(user);
        notification.setRead(false);
        notificationRepository.save(notification);
    }
}