package com.smartfarm.smartfarmmanagementsystem.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class ApiUsage {
    @Id
    private String apiName = "MARKET_API";
    private int callCount = 0;
    private LocalDateTime lastCallDate;
}