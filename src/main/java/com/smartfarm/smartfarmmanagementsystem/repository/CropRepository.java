package com.smartfarm.smartfarmmanagementsystem.repository;

import com.smartfarm.smartfarmmanagementsystem.entity.Crop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CropRepository extends JpaRepository<Crop, Long> {
}