package com.smartfarm.smartfarmmanagementsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MarketPriceDTO {
    private String name;  // Örn: "Armut (Akça)"
    private String price; // Örn: "10.00"
    private String type;  // Örn: "Kg"
}