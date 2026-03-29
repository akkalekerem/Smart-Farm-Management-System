package com.smartfarm.smartfarmmanagementsystem.controller;

import com.smartfarm.smartfarmmanagementsystem.entity.Device;
import com.smartfarm.smartfarmmanagementsystem.entity.Field;
import com.smartfarm.smartfarmmanagementsystem.entity.User;
import com.smartfarm.smartfarmmanagementsystem.repository.DeviceRepository;
import com.smartfarm.smartfarmmanagementsystem.repository.FieldRepository;
import com.smartfarm.smartfarmmanagementsystem.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.Map;
import java.util.Locale;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class FieldController {

    private final FieldRepository fieldRepository;
    private final DeviceRepository deviceRepository;
    private final UserService userService;

    // TARLALARIM SEKMESİ
    @GetMapping("/")
    public String index(Model model, Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            User currentUser = userService.findByEmail(authentication.getName());

            // Kullanıcının tarlalarını ve cihazlarını veritabanından çekiyoruz
            List<Field> myFields = fieldRepository.findByOwner(currentUser);
            List<Device> myDevices = deviceRepository.findByOwner(currentUser);

            model.addAttribute("fields", myFields);
            model.addAttribute("devices", myDevices);
        }

        model.addAttribute("activePage", "home");
        return "pages/index";
    }

    // YENİ TARLA EKLEME
    @PostMapping("/fields/add")
    public String addField(@RequestParam String fieldName,
                           @RequestParam String cropType,
                           @RequestParam Double areaSize,
                           @RequestParam Long deviceId,
                           Authentication authentication) {

        User currentUser = userService.findByEmail(authentication.getName());
        Device selectedDevice = deviceRepository.findById(deviceId).orElse(null);

        Field newField = new Field();
        newField.setFieldName(fieldName);
        newField.setCropType(cropType);
        newField.setAreaSize(areaSize);
        newField.setOwner(currentUser);
        newField.setDevice(selectedDevice); // Tarlaya seçilen cihazı atadık

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
                              @RequestParam String cropType,
                              @RequestParam Double areaSize) {

        Field field = fieldRepository.findById(id).orElseThrow();
        field.setFieldName(fieldName);
        field.setCropType(cropType);
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

    // GRAFİKLER İÇİN RASTGELE SENSÖR VERİSİ ÜRETEN API
    @GetMapping("/api/fields/{id}/telemetry")
    @ResponseBody // Sayfa yerine doğrudan JSON verisi döndürmesini sağlar
    public ResponseEntity<Map<String, Object>> getSimulatedTelemetry(@PathVariable("id") Long id) {

        // Burada her sensör için mantıklı aralıklarda rastgele sayılar üretiyoruz
        double temp = 15.0 + (Math.random() * 20.0); // 15°C ile 35°C arası
        double moisture = 30.0 + (Math.random() * 50.0); // %30 ile %80 arası
        double light = 200.0 + (Math.random() * 800.0); // 200 ile 1000 lüx arası
        double ec = 1.0 + (Math.random() * 1.5); // 1.0 ile 2.5 mS/cm arası
        double wind = Math.random() * 15.0; // 0 ile 15 km/h arası
        boolean isRaining = Math.random() > 0.8; // %20 ihtimalle yağmur yağıyor

        // Verileri JSON formatına dönüştürmek üzere Map'e koyuyoruz
        Map<String, Object> telemetryData = Map.of(
                "temperature", String.format(Locale.US, "%.1f", temp),
                "soilMoisture", String.format(Locale.US, "%.1f", moisture),
                "lightLevel", String.format(Locale.US, "%.0f", light),
                "ecLevel", String.format(Locale.US, "%.2f", ec),
                "windSpeed", String.format(Locale.US, "%.1f", wind),
                "isRaining", isRaining
        );

        return ResponseEntity.ok(telemetryData);
    }
}