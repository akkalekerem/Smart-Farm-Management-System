package com.smartfarm.smartfarmmanagementsystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class SmartFarmManagementSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmartFarmManagementSystemApplication.class, args);
    }

}
