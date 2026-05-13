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
    private String name;

    private Double minTemp;
    private Double maxTemp;
    private Double minMoisture;
    private Double maxMoisture;
    private Double minLight;
    private Double maxLight;
    private Double minEc;
    private Double maxEc;
    private Double maxWindSpeed;

    // Fotoğraf URL'si için yeni alan
    private String imageUrl;

    @Column(length = 1000)
    private String aiSpecialNote;
}