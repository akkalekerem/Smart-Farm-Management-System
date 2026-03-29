package com.smartfarm.smartfarmmanagementsystem.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "fields")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Field {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fieldName;

    private String cropType;

    private Double areaSize;

    // --- İLİŞKİLER ---

    // (Çoklu tarla -> Tek Kullanıcı)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User owner;

    // "Her tarlaya 1 adet cihaz koyulmalı" kuralı gereği OneToOne (Birebir) ilişki kuruyoruz.
    // Bir cihaz bir tarlaya atandığında, tarlanın veritabanı kaydında o cihazın ID'si tutulacak.
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "device_id")
    private Device device;
}