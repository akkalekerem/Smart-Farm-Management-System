package com.smartfarm.smartfarmmanagementsystem.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "crops")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Crop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;           // Örn: Patates, Çilek, Domates

    // --- SICAKLIK PARAMETRELERİ (°C) ---
    private Double minTemp;        // Minimum büyüme sınırı
    private Double maxTemp;        // Maksimum dayanma sınırı (Isı stresi)

    // --- NEM PARAMETRELERİ (%) ---
    private Double minMoisture;    // Kritik sulama sınırı
    private Double maxMoisture;    // Mantar hastalığı riski sınırı

    // --- IŞIK PARAMETRELERİ (Lux) ---
    private Double minLight;       // Minimum fotosentez ışığı
    private Double maxLight;       // Yaprak yanması riski sınırı

    // --- BESİN / GÜBRELEME PARAMETRELERİ (EC - mS/cm) ---
    private Double minEc;          // Minimum besin ihtiyacı
    private Double maxEc;          // Tuzluluk / Aşırı gübreleme sınırı

    // --- RÜZGAR PARAMETRELERİ (km/h) ---
    private Double maxWindSpeed;   // Fiziksel zarar/kırılma sınırı

    @Column(length = 1000)
    private String aiSpecialNote;  // Bu bitkiye özel AI tavsiyesi
}
