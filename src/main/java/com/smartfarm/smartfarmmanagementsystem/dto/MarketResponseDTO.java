package com.smartfarm.smartfarmmanagementsystem.dto;

import lombok.Data;
import java.util.List;

@Data
public class MarketResponseDTO {
    private boolean success;
    private List<MarketPriceDTO> result;
}