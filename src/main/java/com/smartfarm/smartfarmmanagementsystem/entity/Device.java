package com.smartfarm.smartfarmmanagementsystem.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "devices")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Device {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ESP32-S3'ün benzersiz MAC Adresi (Örn: "00:1B:44:11:3A:B7")
    @Column(unique = true, nullable = false)
    private String macAddress;

    // Çiftçinin cihaza verdiği isim (Örn: "Kuzey Serası Ana Ünite")
    private String deviceName;

    // --- TELEMETRİ (DONANIM SAĞLIĞI) VERİLERİ ---

    // Pil yüzdesi (0-100)
    private int batteryLevel;

    // Şarj oluyor mu? (Güneş paneli aktif mi?)
    private boolean isCharging;

    // Wi-Fi sinyal gücü (Örn: -45 dBm)
    private int wifiSignal;

    // Cihaz anlık olarak internete bağlı mı?
    private boolean isOnline;

    // Cihazdan en son ne zaman veri alındı? (Kopmaları tespit etmek için)
    private LocalDateTime lastUpdate;

    // --- ÇEVRE BİRİMLERİ (SENSÖR) DONANIM DURUMLARI ---
    // (Sensörlerin okuduğu tarımsal veriler değil, sensörlerin fiziksel olarak karta bağlı olup olmadığı)

    private boolean soilMoistureActive; // Nem sensörü
    private boolean tempModuleActive;   // Isı sensörü
    private boolean lightModuleActive;  // Işık sensörü
    private boolean rainModuleActive;   // Yağmur sensörü
    private boolean ecModuleActive;     // Toprak EC/pH (Besin) sensörü
    private boolean windModuleActive;   // Rüzgar sensörü

    // --- İLİŞKİLER ---

    // Bu cihaz hangi çiftçiye (User) ait?
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User owner;
}